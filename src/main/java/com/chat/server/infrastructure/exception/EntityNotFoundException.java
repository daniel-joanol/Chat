package com.chat.server.infrastructure.exception;

public class EntityNotFoundException extends RuntimeException {
  
  public EntityNotFoundException(String message) {
    super(message);
  }

}
