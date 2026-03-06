package com.chat.server.infrastructure.exception;

public final class ForbiddenException extends AbstractException {
  
  public ForbiddenException(String internalMessage) {
    super("Forbidden", internalMessage);
  }
}
