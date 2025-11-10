package com.chat.server.infrastructure.exception.response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.MethodArgumentNotValidException;

import com.chat.server.infrastructure.exception.AbstractException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponseFactory {
  
  public static ErrorResponse getResponse(Exception e, UUID traceId) {

    if  (e instanceof AbstractException) {
      var absE = (AbstractException) e; 
      return new ErrorResponse(traceId, absE.getExternalMessage());

    } else if (e instanceof MethodArgumentNotValidException) {
      var validE = (MethodArgumentNotValidException) e;
      List<String> messages = new ArrayList<>();
      for (var error : validE.getAllErrors()) messages.add(error.getDefaultMessage());
      return new ErrorResponse(traceId, String.join("; ", messages));
    
    } else {
      return new ErrorResponse(traceId, "Internal Error");
    }
  }

}
