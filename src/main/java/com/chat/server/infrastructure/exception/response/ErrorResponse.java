package com.chat.server.infrastructure.exception.response;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
  
  private ZonedDateTime timestamp;
  private UUID traceId;
  private String message;

}
