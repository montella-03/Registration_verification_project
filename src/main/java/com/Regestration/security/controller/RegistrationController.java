package com.Regestration.security.controller;


import com.Regestration.security.Entity.User;
import com.Regestration.security.events.RegistrationEvent;
import com.Regestration.security.model.UserModel;
import com.Regestration.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher publisher;

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
