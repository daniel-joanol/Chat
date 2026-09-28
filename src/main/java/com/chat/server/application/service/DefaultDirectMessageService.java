package com.chat.server.application.service;

import org.springframework.stereotype.Service;

import com.chat.server.domain.dao.ContactDao;
import com.chat.server.domain.dao.DirectMessageDao;
import com.chat.server.domain.model.DirectMessage;
import com.chat.server.domain.model.User;
import com.chat.server.domain.service.DirectMessageService;
import com.chat.server.domain.service.UserService;
import com.chat.server.domain.util.SecurityUtil;
import com.chat.server.infrastructure.exception.BadRequestException;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.exception.ForbiddenException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultDirectMessageService implements DirectMessageService {

  private final DirectMessageDao dao;
  private final ContactDao contactDao;
  private final SecurityUtil securityUtil;
  private final UserService userService;

  @Override
  public DirectMessage send(String recipientUsername, String content)
      throws BadRequestException, ForbiddenException, EntityNotFoundException {
    String senderUsername = securityUtil.getUsername();
    if (recipientUsername.equals(senderUsername)) {
      String message = String.format("User %s trying to message itself", senderUsername);
      throw new BadRequestException(message);
    }

    if (content == null || content.isBlank()) {
      throw new BadRequestException("Message content cannot be blank");
    }

    User sender = userService.getByUsername(senderUsername);
    User recipient = userService.getByUsername(recipientUsername);
    if (!contactDao.exists(senderUsername, recipientUsername)) {
      String message = String.format("User %s cannot message non-contact %s", senderUsername, recipientUsername);
      throw new ForbiddenException(message);
    }

    DirectMessage directMessage = DirectMessage.fromCreationRequest(sender, recipient, content);
    return dao.save(directMessage);
  }

}