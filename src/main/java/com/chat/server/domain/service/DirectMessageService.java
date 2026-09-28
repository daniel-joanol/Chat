package com.chat.server.domain.service;

import com.chat.server.domain.model.DirectMessage;
import com.chat.server.infrastructure.exception.BadRequestException;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.exception.ForbiddenException;

public interface DirectMessageService {

  DirectMessage send(String recipientUsername, String content)
      throws BadRequestException, ForbiddenException, EntityNotFoundException;

}