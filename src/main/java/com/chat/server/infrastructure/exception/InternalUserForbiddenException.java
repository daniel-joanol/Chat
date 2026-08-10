package com.chat.server.infrastructure.exception;

public final class InternalUserForbiddenException extends CheckedException {
  
  public InternalUserForbiddenException(String internalMessage) {
    super("Something bad happened", internalMessage);
  }

  public InternalUserForbiddenException(String internalMessage, Throwable cause) {
    super("Something bad happened", internalMessage, cause);
  }
}
