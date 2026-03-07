package com.chat.server.infrastructure.exception;

public final class EntityNotFoundException extends AbstractException {

  public EntityNotFoundException(String internalMessage) {
    super(internalMessage);
  }

}
