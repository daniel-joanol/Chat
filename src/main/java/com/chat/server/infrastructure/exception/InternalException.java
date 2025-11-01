package com.chat.server.infrastructure.exception;

public class InternalException extends AbstractException {
  
  public InternalException(String externalMessage) {
    super("Internal Error.", externalMessage);
  }

  public InternalException(String externalMessage, Throwable throwable) {
    super("Internal Error.", externalMessage, throwable);
  }

}
