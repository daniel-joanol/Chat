package com.chat.server.domain.service;

import java.util.UUID;

import com.chat.server.domain.model.Contact;

public interface ContactService {
  
  Contact addContact(String contactUsername);

  void delete(UUID id);

}
