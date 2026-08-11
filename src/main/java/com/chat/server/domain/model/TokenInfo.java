package com.chat.server.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record TokenInfo(
    String accessToken,
    LocalDateTime expiresAt
) {}
