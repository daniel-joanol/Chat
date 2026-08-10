package com.chat.server.infrastructure.exception;

public final class ConflictException extends CheckedException {

  public ConflictException(String message) {
    super(message);
  }
}
