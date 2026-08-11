package com.chat.server.infrastructure.exception;

public final class EntityNotFoundException extends CheckedException {

  public EntityNotFoundException(String internalMessage) {
    super(internalMessage);
  }

}
