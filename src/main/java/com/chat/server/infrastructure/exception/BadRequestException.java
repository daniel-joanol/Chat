package com.chat.server.infrastructure.exception;

public final class BadRequestException extends AbstractException {

  public BadRequestException(String message) {
    super(message, message);
  }
  
}
