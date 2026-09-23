package com.chat.server.infrastructure.exception.websocket;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.chat.server.domain.constants.Constants;
import com.chat.server.infrastructure.exception.BadRequestException;
import com.chat.server.infrastructure.exception.CheckedException;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.exception.ForbiddenException;
import com.chat.server.infrastructure.exception.response.WebSocketErrorResponse;

@ControllerAdvice
public class GlobalWebSocketExceptionHandler {

  @MessageExceptionHandler({BadRequestException.class, ForbiddenException.class, EntityNotFoundException.class})
  @SendToUser(value = Constants.WEBSOCKET_ERROR_DESTINATION, broadcast = false)
  public WebSocketErrorResponse handleMessageException(CheckedException exception) {
    return new WebSocketErrorResponse(
        exception.getClass().getSimpleName(),
        exception.getExternalMessage());
  }

}