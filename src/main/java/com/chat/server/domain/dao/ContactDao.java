package com.chat.server.domain.dao;

import java.util.UUID;

import com.chat.server.domain.model.Contact;

public interface ContactDao {
  
  Contact save(Contact contact);

  boolean exists(String userUsername, String contactUsername);

  Contact getById(UUID id);

  void delete(UUID id);

}
