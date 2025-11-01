package com.chat.server.domain.dao;

import com.chat.server.domain.model.User;

public interface UserDao {

  User save(User user);
  
  User getByUsername(String username);

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);



}
