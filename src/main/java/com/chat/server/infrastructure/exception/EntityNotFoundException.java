package com.chat.server.infrastructure.exception;

public class EntityNotFoundException extends AbstractException {
  
  public EntityNotFoundException(String externalMessage, String internalMessage) {
    super(externalMessage, internalMessage);
  }

  public EntityNotFoundException(String externalMessage, String internalMessage, Throwable throwable) {
    super(externalMessage, internalMessage, throwable);
  }

}
