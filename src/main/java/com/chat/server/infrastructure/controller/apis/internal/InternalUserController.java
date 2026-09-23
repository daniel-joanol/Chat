package com.chat.server.infrastructure.controller.apis.internal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.server.domain.constants.Constants;
import com.chat.server.domain.service.UserService;
import com.chat.server.domain.util.SecurityUtil;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.exception.InternalException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.INTERNAL_USER_CONTROLLER)
@PreAuthorize(Constants.HAS_ROLE_USER)
@Tag(name = "Internal User Controller", description = "Authenticated user operations")
public class InternalUserController {

  private final UserService userService;
  private final SecurityUtil securityUtil;

  @Operation(summary = "Logout", description = "Mark the authenticated user as offline")
  @ApiResponse(responseCode = "204", description = "User logged out")
  @PostMapping("/logout")
  public ResponseEntity<Void> logout() throws InternalException, EntityNotFoundException {
    userService.logout(securityUtil.getUsername());
    return ResponseEntity.noContent().build();
  }

}