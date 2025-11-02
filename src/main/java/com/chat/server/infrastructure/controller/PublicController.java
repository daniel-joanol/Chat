package com.chat.server.infrastructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.server.domain.constants.Constants;
import com.chat.server.domain.model.User;
import com.chat.server.domain.model.UserFactory;
import com.chat.server.domain.service.UserService;
import com.chat.server.infrastructure.controller.mapper.UserDtoMapper;
import com.chat.server.infrastructure.controller.request.UserRequest;
import com.chat.server.infrastructure.controller.response.UserResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.PUBLIC_CONTROLLER)
public class PublicController {

  private final UserService service;
  private final UserDtoMapper mapper;
  
  @PostMapping("/user")
  public ResponseEntity<UserResponse> createUser(
      @Valid UserRequest request
  ) {
    User user = mapper.toDomain(request);
    user = UserFactory.generateInternalUser(user);
    user = service.createUser(user);
    UserResponse response = mapper.toResponse(user);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);
  }

}
