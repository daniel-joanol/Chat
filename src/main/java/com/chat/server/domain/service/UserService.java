package com.chat.server.domain.service;

import com.chat.server.domain.model.User;

public interface UserService {

  User getByUsername(String username);
  
  String authenticate(String username, String password);

  User createUser(User user);

  void updatePassword(User user);

  void deleteUser(String username);
  
}
