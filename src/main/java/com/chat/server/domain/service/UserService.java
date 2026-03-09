package com.chat.server.domain.service;

import java.util.List;

import com.chat.server.domain.model.User;

public interface UserService {

  User getByUsername(String username);

  User createUser(User user);

  void updatePassword(User user);

  void deleteUser(User user);

  void deleteUser(String username);

  List<User> getIncompleteUsers();
  
}
