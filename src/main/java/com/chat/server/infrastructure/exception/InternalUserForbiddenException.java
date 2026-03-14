package com.chat.server.infrastructure.exception;

public final class InternalUserForbiddenException extends AbstractException {
  
  public InternalUserForbiddenException(String internalMessage) {
    super("Something bad happened", internalMessage);
  }
}
