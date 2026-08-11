package com.chat.server.infrastructure.exception;

public final class ForbiddenException extends CheckedException {
  
  public ForbiddenException(String internalMessage) {
    super("Forbidden", internalMessage);
  }
  
}
