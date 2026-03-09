package com.chat.server.domain.dao;

import java.util.UUID;

import com.chat.server.domain.model.TokenInfo;
import com.chat.server.domain.model.User;

public interface AccessManagementDao {
  
  TokenInfo authenticate(String username, String password);

  void createUser(String jwt, User user);

  User getUser(String jwt, String username);

  void updatePassword(String jwt, User user);

  void addRole(String jwt, User user);

  void deleteUser(String jwt, UUID userId);

}
