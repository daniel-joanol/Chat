package com.chat.server.infrastructure.exception.websocket;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.chat.server.infrastructure.exception.BadRequestException;

class GlobalWebSocketExceptionHandlerTest {

  private GlobalWebSocketExceptionHandler sut = new GlobalWebSocketExceptionHandler();

  @Test
  void testHandleMessageException_returnSafeErrorResponse() {
    var exception = new BadRequestException("Message content cannot be blank");

    var response = sut.handleMessageException(exception);

    assertEquals("BadRequestException", response.code());
    assertEquals(exception.getExternalMessage(), response.message());
  }

}