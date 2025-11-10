package com.chat.server.domain.dao;

import java.util.UUID;

import com.chat.server.domain.model.User;

public interface UserDao {

  User getById(UUID id);

  User save(User user);
  
  User getByUsername(String username);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  void delete(UUID id);

}
