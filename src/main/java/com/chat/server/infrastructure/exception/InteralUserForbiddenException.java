package com.chat.server.infrastructure.exception;

public final class InteralUserForbiddenException extends AbstractException {
  
  public InteralUserForbiddenException(String internalMessage) {
    super("Something bad happened", internalMessage);
  }
}
