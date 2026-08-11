package com.chat.server.infrastructure.exception;

public final class InternalException extends UncheckedException {
  
  private static final String EXTERNAL_MESSAGE = "Internal Error.";

  public InternalException(String internalMessage) {
    super(EXTERNAL_MESSAGE, internalMessage);
  }

}
