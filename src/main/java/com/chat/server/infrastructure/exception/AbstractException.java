package com.chat.server.infrastructure.exception;

import lombok.Getter;

@Getter
public abstract class AbstractException extends RuntimeException {
  
  private final String externalMessage;
  private final String internalMessage;
  private final Throwable cause;

  protected AbstractException(String message) {
    super(message);
    this.internalMessage = message;
    this.externalMessage = message;
    this.cause = null;
  }

  protected AbstractException(String message, Throwable cause) {
    super(message, cause);
    this.internalMessage = message;
    this.externalMessage = message;
    this.cause = cause;
  }
  
  protected AbstractException(String externalMessage, String internalMessage) {
    super(internalMessage);
    this.externalMessage = externalMessage;
    this.internalMessage = internalMessage;
    this.cause = null;
  }

  protected AbstractException(String externalMessage, String internalMessage, Throwable cause) {
    super(internalMessage, cause);
    this.externalMessage = externalMessage;
    this.internalMessage = internalMessage;
    this.cause = cause;
  }

}
