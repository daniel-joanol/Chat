package com.chat.server.domain.service;

import com.chat.server.domain.model.User;

public interface AccessManagementService {
  
  String authenticate(String username, char[] password);

  User createUser(User user);

  void updatePassword(User user);

  void deleteUser(String username);
  
}
