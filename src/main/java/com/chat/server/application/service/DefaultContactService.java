package com.chat.server.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.chat.server.domain.dao.ContactDao;
import com.chat.server.domain.model.Contact;
import com.chat.server.domain.service.ContactService;
import com.chat.server.domain.service.UserService;
import com.chat.server.domain.util.SecurityUtil;
import com.chat.server.infrastructure.exception.BadRequestException;
import com.chat.server.infrastructure.exception.ConflictException;
import com.chat.server.infrastructure.exception.ForbiddenException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultContactService implements ContactService {
  
  private final ContactDao dao;
  private final SecurityUtil secUtil;
  private final UserService userService;
  
  @Override
  public Contact addContact(String contactUsername) {
    var username = secUtil.getUsername();
    if (contactUsername.equals(username)) {
      var message = String.format("User trying to add itself %s", username);
      throw new BadRequestException(message);
    }

    if (dao.exists(username, contactUsername)) {
      var message = String.format("Contact %s already associated to user %s", contactUsername, username);
      throw new ConflictException(message);
    }

    var user = userService.getByUsername(username);
    var friend = userService.getByUsername(contactUsername);
    var contact = Contact.fromCreationRequest(user, friend);
    return dao.save(contact);
  }

  @Override
  public void delete(UUID id) {
    var username = secUtil.getUsername();
    var contact = dao.getById(id);
    boolean contactBelongsToAnotherUser = contact.getUser().getUsername().equals(username);
    if (contactBelongsToAnotherUser) {
      var message = String.format("Contact %s does not belong to user %s", id, username);
      throw new ForbiddenException(message);
    
    } else {
      dao.delete(id);
    }
  }

}
