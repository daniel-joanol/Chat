package com.chat.server.infrastructure.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DirectMessageRequest(
    @NotBlank String recipientUsername,
    @NotBlank @Size(max = 4000) String content
) {}