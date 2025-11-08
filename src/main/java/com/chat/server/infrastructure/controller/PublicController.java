package com.chat.server.infrastructure.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.server.domain.constants.Constants;
import com.chat.server.domain.model.User;
import com.chat.server.domain.model.UserFactory;
import com.chat.server.domain.service.UserService;
import com.chat.server.infrastructure.controller.mapper.UserDtoMapper;
import com.chat.server.infrastructure.controller.request.LoginRequest;
import com.chat.server.infrastructure.controller.request.UserRequest;
import com.chat.server.infrastructure.controller.response.UserResponse;
import com.chat.server.infrastructure.exception.AuthenticationFailedException;
import com.chat.server.infrastructure.exception.EntityNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.PUBLIC_CONTROLLER)
@Tag(
    name = "Public Controller",
    description = "Controller to manage petitions that do not require authentication"
)
public class PublicController {

  private final UserService service;
  private final UserDtoMapper mapper;

  @Operation(
      summary = "Login",
      description = """
          Endpoint to login.
          Every parameter is mandatory.
          """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Login successful."),
      @ApiResponse(responseCode = "400", description = "Mandatory parameter missing."),
      @ApiResponse(responseCode = "401", description = "Unauthorized.")
  })
  @PostMapping("/login")
  public ResponseEntity<String> login(
      @Valid @RequestBody LoginRequest request
  ) {
    try {
      String token = service.authenticate(request.username(), request.password());
      return ResponseEntity
          .status(HttpStatus.OK)
          .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE)
          .body(token);

    } catch (EntityNotFoundException e) {
      throw new AuthenticationFailedException(e.getInternalMessage(), e);
    }
  } 

  @Operation(
      summary = "Create user",
      description = """
          Endpoint used to create external users.
          Every parameter is manadatory.
          Password must contain between 12 and 30 characters, including lowcase, highcase, numbers and special characters.
      """
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "User created."),
      @ApiResponse(responseCode = "400", description = "Mandatory parameter missing or invalid password."),
      @ApiResponse(responseCode = "409", description = "Duplicated value.")
  })
  @PostMapping("/user")
  public ResponseEntity<UserResponse> createUser(
      @Valid @RequestBody UserRequest request
  ) {
    User user = mapper.toDomain(request);
    user = UserFactory.generateExternalUser(user);
    user = service.createUser(user);
    UserResponse response = mapper.toResponse(user);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);
  }

}
