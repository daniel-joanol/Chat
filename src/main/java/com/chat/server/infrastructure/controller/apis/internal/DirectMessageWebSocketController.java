package com.chat.server.infrastructure.controller.apis.internal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.chat.server.domain.constants.Constants;
import com.chat.server.domain.model.DirectMessage;
import com.chat.server.domain.service.DirectMessageService;
import com.chat.server.infrastructure.controller.mapper.DirectMessageDtoMapper;
import com.chat.server.infrastructure.controller.request.DirectMessageRequest;
import com.chat.server.infrastructure.controller.response.DirectMessageResponse;
import com.chat.server.infrastructure.exception.BadRequestException;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.exception.ForbiddenException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DirectMessageWebSocketController {

  private final DirectMessageService service;
  private final DirectMessageDtoMapper mapper;
  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping(Constants.DIRECT_MESSAGE_MAPPING)
  public void send(@Valid DirectMessageRequest request)
      throws BadRequestException, ForbiddenException, EntityNotFoundException {
    DirectMessage message = service.send(request.recipientUsername(), request.content());
    DirectMessageResponse response = mapper.toResponse(message);
    messagingTemplate.convertAndSendToUser(
        response.recipientUsername(),
        Constants.DIRECT_MESSAGE_USER_DESTINATION,
        response);
  }

}