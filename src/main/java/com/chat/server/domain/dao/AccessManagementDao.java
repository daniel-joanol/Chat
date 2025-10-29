package com.chat.server.domain.dao;

import java.util.UUID;

import com.chat.server.domain.model.User;

public interface AccessManagementDao {
  
  String login(String username, char[] password);

  String createUser(String jwt, User user);

  void deleteUser(String jwt, UUID userId);

}
