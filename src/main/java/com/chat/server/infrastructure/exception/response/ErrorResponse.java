package com.chat.server.infrastructure.exception.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
  
  private UUID traceId;
  private String message;

}
