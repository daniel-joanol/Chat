package com.chat.server.domain.service;

import java.util.UUID;

import com.chat.server.domain.model.User;

public interface AccessManagementService {
  
  String login(String username, char[] password);

  String createUser(User user);

  void updatePassword(User user);

  void deleteUser(UUID userId);
  
}
