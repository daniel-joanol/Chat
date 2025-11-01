package com.chat.server.infrastructure.exception;

import lombok.Getter;

@Getter
public abstract class AbstractException extends RuntimeException {
  
  private String externalMessage;
  private String internalMessage;
  
  public AbstractException(String externalMessage, String internalMessage) {
    super(internalMessage);
    this.externalMessage = externalMessage;
    this.internalMessage = internalMessage;
  }

  public AbstractException(String externalMessage, String internalMessage, Throwable throwable) {
    super(internalMessage, throwable);
    this.externalMessage = externalMessage;
    this.internalMessage = internalMessage;
  }

}
