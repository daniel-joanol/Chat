package com.chat.server.infrastructure.exception;

import lombok.Getter;

@Getter
public abstract class CheckedException extends Exception {
  
  private final String externalMessage;
  private final String internalMessage;
  private final Throwable cause;

  protected CheckedException(String message) {
    super(message);
    this.internalMessage = message;
    this.externalMessage = message;
    this.cause = null;
  }

  protected CheckedException(String message, Throwable cause) {
    super(message, cause);
    this.internalMessage = message;
    this.externalMessage = message;
    this.cause = cause;
  }
  
  protected CheckedException(String externalMessage, String internalMessage) {
    super(internalMessage);
    this.externalMessage = externalMessage;
    this.internalMessage = internalMessage;
    this.cause = null;
  }

  protected CheckedException(String externalMessage, String internalMessage, Throwable cause) {
    super(internalMessage, cause);
    this.externalMessage = externalMessage;
    this.internalMessage = internalMessage;
    this.cause = cause;
  }

}
