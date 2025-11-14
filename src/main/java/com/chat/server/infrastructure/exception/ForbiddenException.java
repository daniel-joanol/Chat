package com.chat.server.infrastructure.exception;

public class ForbiddenException extends AbstractException {
  
  public ForbiddenException(String internalMessage) {
    super("Forbidden", internalMessage);
  }
}
