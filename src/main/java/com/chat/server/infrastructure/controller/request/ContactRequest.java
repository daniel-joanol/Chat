package com.chat.server.infrastructure.controller.request;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContactRequest(
    @NotNull UUID userId,
    @NotBlank String contactUsername
) {}
