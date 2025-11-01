package com.chat.server.domain.service;

import java.util.UUID;

import com.chat.server.domain.model.User;

public interface UserService {
  
  User save(User user);

  User getByUsername(String username);

  User getById(UUID id);

  boolean existsByUsername(String username);

}
