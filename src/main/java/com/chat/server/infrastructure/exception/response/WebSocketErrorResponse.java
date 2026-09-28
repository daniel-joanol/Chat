package com.chat.server.infrastructure.exception.response;

public record WebSocketErrorResponse(
    String code,
    String message
) {}