package com.chat.server.infrastructure.exception;

public class AuthenticationFailedException extends RuntimeException {
  
  public AuthenticationFailedException() {
    super("Authentication failed.");
  }
}
