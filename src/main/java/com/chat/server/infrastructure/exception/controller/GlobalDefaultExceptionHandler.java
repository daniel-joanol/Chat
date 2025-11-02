package com.chat.server.infrastructure.exception.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.chat.server.infrastructure.exception.AbstractException;
import com.chat.server.infrastructure.exception.AuthenticationFailedException;
import com.chat.server.infrastructure.exception.ConflictException;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.exception.InternalException;
import com.chat.server.infrastructure.exception.response.ErrorResponse;
import com.chat.server.infrastructure.exception.response.ErrorResponseFactory;

import jakarta.servlet.http.HttpServletRequest;
import kong.unirest.core.UnirestConfigException;
import kong.unirest.core.UnirestException;
import kong.unirest.core.UnirestParsingException;
import kong.unirest.core.json.JSONException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalDefaultExceptionHandler {

  @ExceptionHandler(AuthenticationFailedException.class)
  public ResponseEntity<ErrorResponse> handleAuthenticationFailedException(AuthenticationFailedException e, HttpServletRequest req) {
    ErrorResponse response = this.generateResponse(e);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest req) {
    ErrorResponse response = this.generateResponse(e);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorResponse> handleConflictException(ConflictException e, HttpServletRequest req) {
    ErrorResponse response = this.generateResponse(e);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(InternalException.class)
  public ResponseEntity<ErrorResponse> handleInternalException(InternalException e, HttpServletRequest req) {
    ErrorResponse response = this.generateResponse(e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler({JSONException.class, UnirestException.class, UnirestConfigException.class, UnirestParsingException.class})
  public ResponseEntity<ErrorResponse> handleGenericException(Exception e, HttpServletRequest req) {
    ErrorResponse response = this.generateResponse(e);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
  
  private ErrorResponse generateResponse(Exception e) {
    UUID traceId = UUID.randomUUID();
    String message = String.format("[%s] %s", traceId, e.getMessage());
    log.error(message, e);
    return ErrorResponseFactory.getResponse(e, traceId);
  }

  private ErrorResponse generateResponse(AbstractException e) {
    UUID traceId = UUID.randomUUID();
    String message = String.format("[%s] %s", traceId, e.getInternalMessage());
    log.error(message, e);
    return ErrorResponseFactory.getResponse(e, traceId);
  }
  
}
