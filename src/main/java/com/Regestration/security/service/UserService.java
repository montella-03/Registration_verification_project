package com.Regestration.security.service;


import com.Regestration.security.Entity.User;
import com.Regestration.security.Entity.Verification;
import com.Regestration.security.model.UserModel;

import java.util.Optional;

public interface UserService {

 public User registerUser(UserModel userModel);

 public void saveVerificationToken(String token, User user);

 public String validateToken(String token);

 public Verification generateNewVerificationToken(String oldToken);

 public User findUserByEmail(String email);

 public void createPasswordResetToken(User user, String token);

 public String validatePasswordReset(String token);

 Optional<User> findUserByToken(String token);

 void changePassword(User user, String newPassword);
}


