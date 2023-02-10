package com.Regestration.security.service;


import com.Regestration.security.Entity.User;
import com.Regestration.security.model.UserModel;

public interface UserService {

  public  User registerUser(UserModel userModel);

 public  void saveVerificationToken(String token, User user);
}
