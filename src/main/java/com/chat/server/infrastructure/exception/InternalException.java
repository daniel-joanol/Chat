package com.chat.server.infrastructure.exception;

public class InternalException extends RuntimeException {
  
  public InternalException() {
    super("Internal Error.");
  }
}
