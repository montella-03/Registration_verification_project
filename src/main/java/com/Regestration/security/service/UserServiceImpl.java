package com.Regestration.security.service;

import com.Regestration.security.Entity.PasswordResetToken;
import com.Regestration.security.Entity.User;
import com.Regestration.security.Entity.Verification;
import com.Regestration.security.Repository.PasswordResetRepository;
import com.Regestration.security.Repository.UserRepository;
import com.Regestration.security.Repository.VerificationRepository;
import com.Regestration.security.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationRepository verificationRepository;
    @Autowired
    private PasswordResetRepository passwordResetRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        user.setRole("Admin");
        userRepository.save(user);
        return user;
    }

    @Override
    public void saveVerificationToken(String token, User user) {
        Verification verification = new Verification(user,token);
        verificationRepository.save(verification);

    }

    @Override
    public String validateToken(String token) {
        Verification verification = verificationRepository.findByToken(token);
        if(verification==null){
            return "invalid";
        }
        User user = verification.getUser();
        Calendar calendar = Calendar.getInstance();
        if(verification.getExpireTime().getTime()-calendar.getTime().getTime()<=0){
            verificationRepository.delete(verification);
            return "expired token";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public Verification generateNewVerificationToken(String oldToken) {
        Verification verification = verificationRepository.findByToken(oldToken);
        verification.setToken(UUID.randomUUID().toString());
        verificationRepository.save(verification);
        return verification;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);

    }

    @Override
    public void createPasswordResetToken(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user,token);
        passwordResetRepository.save(passwordResetToken);

    }

    @Override
    public String validatePasswordReset(String token) {
        PasswordResetToken passwordResetToken = passwordResetRepository.findByToken(token);
        if(passwordResetToken==null){
            return "invalid";
        }
        User user = passwordResetToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if(passwordResetToken.getExpireTime().getTime()-calendar.getTime().getTime()<=0){
            passwordResetRepository.delete(passwordResetToken);
            return "expired token";
        }

        return "valid";
    }

    @Override
    public Optional<User> findUserByToken(String token) {
        return Optional.ofNullable(passwordResetRepository.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
