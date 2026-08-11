package com.chat.server.infrastructure.exception;

public final class BadRequestException extends CheckedException {

  public BadRequestException(String message) {
    super(message);
  }
  
}
