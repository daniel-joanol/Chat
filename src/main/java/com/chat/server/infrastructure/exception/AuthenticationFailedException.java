package com.chat.server.infrastructure.exception;

public class AuthenticationFailedException extends AbstractException {
  
  public AuthenticationFailedException(String internalMessage) {
    super("Authentication failed.", internalMessage);
  }

  public AuthenticationFailedException(String internalMessage, Throwable throwable) {
    super("Authentication failed.", internalMessage, throwable);
  }
  
}
