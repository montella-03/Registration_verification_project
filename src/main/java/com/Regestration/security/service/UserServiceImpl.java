package com.Regestration.security.service;

import com.Regestration.security.Entity.User;
import com.Regestration.security.Entity.Verification;
import com.Regestration.security.Repository.UserRepository;
import com.Regestration.security.Repository.VerificationRepository;
import com.Regestration.security.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationRepository verificationRepository;
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
}
