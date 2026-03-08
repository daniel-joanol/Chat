package com.chat.server.domain.service;

import java.util.List;
import java.util.UUID;

import com.chat.server.domain.model.User;

public interface UserService {

  User getByUsername(String username);
  
  String authenticate(String username, String password);

  User createUser(User user);

  void updatePassword(User user);

  void deleteUser(UUID id);

  void deleteUser(String username);

  List<User> getIncompleteUsers();
  
}
