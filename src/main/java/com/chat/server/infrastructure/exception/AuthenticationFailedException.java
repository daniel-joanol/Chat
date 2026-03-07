package com.chat.server.infrastructure.exception;

public final class AuthenticationFailedException extends AbstractException {

  private static final String EXTERNAL_MESSAGE = "Authentication failed.";
  
  public AuthenticationFailedException(String internalMessage) {
    super(EXTERNAL_MESSAGE, internalMessage);
  }

  public AuthenticationFailedException(String internalMessage, Throwable cause) {
    super(EXTERNAL_MESSAGE, internalMessage, cause);
  }
  
}
