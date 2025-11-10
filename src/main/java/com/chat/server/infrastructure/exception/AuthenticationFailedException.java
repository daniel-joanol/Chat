package com.chat.server.infrastructure.exception;

public class AuthenticationFailedException extends AbstractException {

  private static final String externalMessage = "Authentication failed.";
  
  public AuthenticationFailedException(String internalMessage) {
    super(externalMessage, internalMessage);
  }

  public AuthenticationFailedException(String internalMessage, Throwable cause) {
    super(externalMessage, internalMessage, cause);
  }
  
}
