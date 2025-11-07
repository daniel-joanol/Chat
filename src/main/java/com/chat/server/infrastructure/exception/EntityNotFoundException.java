package com.chat.server.infrastructure.exception;

public class EntityNotFoundException extends AbstractException {

  public EntityNotFoundException(String internalMessage) {
    super(internalMessage);
  }

}
