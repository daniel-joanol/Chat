package com.chat.server.infrastructure.exception;

import lombok.Getter;

@Getter
public class UncheckedException extends RuntimeException {

  private final String internalMessage;
  private final String externalMessage;

  public UncheckedException(String externalMessage, String internalMessage) {
    super(internalMessage);
    this.internalMessage = internalMessage;
    this.externalMessage = externalMessage;
  }
}
