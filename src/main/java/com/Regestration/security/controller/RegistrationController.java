package com.Regestration.security.controller;


import com.Regestration.security.Entity.User;
import com.Regestration.security.Entity.Verification;
import com.Regestration.security.events.RegistrationEvent;
import com.Regestration.security.model.PasswordModel;
import com.Regestration.security.model.UserModel;
import com.Regestration.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, final HttpServletRequest request){
      User user = userService.findUserByEmail(passwordModel.getEmail());
      String url="";
      if(user!=null){
          String token = UUID.randomUUID().toString();
          userService.createPasswordResetToken(user,token);
          url=passwordResetTokenMail(user,applicationUrl(request),token);
      }
      return url;

    }
    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel){
        User user=userService.findUserByEmail(passwordModel.getEmail());
        if(!userService.checkIfOldPassword(user,passwordModel.getOldPassword())){
            return "Invalid old password";
        }
        //save new password and return new password saved successfully
        userService.changePassword(user,passwordModel.getNewPassword());
        return "password changed successfully";

    }
    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token,
                               @RequestBody PasswordModel passwordModel){
        String result = userService.validatePasswordReset(token);
        if(!result.equalsIgnoreCase("valid")){
            return "Invalid";
        }
        Optional<User> user = userService.findUserByToken(token);
        if(user.isPresent()){
            userService.changePassword(user.get(),passwordModel.getNewPassword());
            return "password reset successfully";
        }
        else
            return "Invalid Token";

    }

    private String passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl+"/savePassword?token="+token;
        log.info("click the link to reset your password:{}",url);
        return  url;
    }

    @GetMapping("/verifyToken")
    public String verification(@RequestParam("token") String token){
        String result = userService.validateToken(token);
        if(result.equalsIgnoreCase("valid")){
            return "user verified!";
        }
        return "incorrect token";
    }
    @GetMapping("/resendVerificationToken")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request){
        Verification verification = userService.generateNewVerificationToken(oldToken);
        User user = verification.getUser();
        resendVerificationTokenMail(user,applicationUrl(request),verification);
        return "Verification token resend";
    }

    private void resendVerificationTokenMail(User user, String applicationUrl, Verification token) {
        String url = applicationUrl+"/verifyToken?token= "+token.getToken();
        // here is where you send emails to users
        log.info("click the link to verify your account:{}",url);
    }


    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
    User user = userService.registerUser(userModel);
    publisher.publishEvent(new RegistrationEvent(user,applicationUrl(request)));
    return "success";
}

    private String applicationUrl(HttpServletRequest request) {
    return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
