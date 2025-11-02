package com.chat.server.infrastructure.exception;

public class AuthenticationFailedException extends AbstractException {
  
  public AuthenticationFailedException(String internalMessage) {
    super("Authentication failed.", internalMessage);
  }
  
}
