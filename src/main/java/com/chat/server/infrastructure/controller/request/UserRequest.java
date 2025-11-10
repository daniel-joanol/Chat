package com.chat.server.infrastructure.controller.request;

import com.chat.server.infrastructure.controller.annotation.ValidPassword;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
    @NotBlank String username,
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank String email,
    @ValidPassword String password
) {}
