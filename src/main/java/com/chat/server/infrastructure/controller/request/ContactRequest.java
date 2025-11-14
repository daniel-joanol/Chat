package com.chat.server.infrastructure.controller.request;

import jakarta.validation.constraints.NotBlank;

public record ContactRequest(
    @NotBlank String contactUsername
) {}
