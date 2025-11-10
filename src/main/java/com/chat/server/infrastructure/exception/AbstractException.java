package com.chat.server.infrastructure.exception;

import lombok.Getter;

@Getter
public abstract class AbstractException extends RuntimeException {
  
  private String externalMessage;
  private String internalMessage;

  public AbstractException(String message) {
    super(message);
    this.internalMessage = message;
    this.externalMessage = message;
  }

  public AbstractException(String message, Throwable cause) {
    super(message, cause);
    this.internalMessage = message;
    this.externalMessage = message;
  }
  
  public AbstractException(String externalMessage, String internalMessage) {
    super(internalMessage);
    this.externalMessage = externalMessage;
    this.internalMessage = internalMessage;
  }

  public AbstractException(String externalMessage, String internalMessage, Throwable cause) {
    super(internalMessage, cause);
    this.externalMessage = externalMessage;
    this.internalMessage = internalMessage;
  }

}
