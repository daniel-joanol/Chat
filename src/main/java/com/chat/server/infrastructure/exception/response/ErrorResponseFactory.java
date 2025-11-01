package com.chat.server.infrastructure.exception.response;

import java.util.UUID;

import com.chat.server.infrastructure.exception.AbstractException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseFactory {
  
  public static ErrorResponse getResponse(Exception e, UUID traceId) {

    if  (e instanceof AbstractException) {
      AbstractException absE = (AbstractException) e; 
      return new ErrorResponse(traceId, absE.getExternalMessage());
    
    } else {
      return new ErrorResponse(traceId, e.getMessage());
    }
  }

}
