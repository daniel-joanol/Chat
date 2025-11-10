package com.chat.server.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.util.ReflectionTestUtils;

import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.domain.model.UserFactory;
import com.chat.server.domain.service.UserService;
import com.chat.server.infrastructure.controller.mapper.UserDtoMapper;
import com.chat.server.infrastructure.controller.request.LoginRequest;
import com.chat.server.infrastructure.controller.request.UserRequest;
import com.chat.server.infrastructure.exception.AuthenticationFailedException;
import com.chat.server.infrastructure.exception.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class PublicControllerTest {
  
  private UserDtoMapper mapper = Mappers.getMapper(UserDtoMapper.class);
  private LoginRequest loginRequest = new LoginRequest("username", "pass");

  @Mock
  private UserService service;

  @InjectMocks
  private PublicController sut;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(sut, "mapper", mapper);
  }

  @Test
  void testLogin_returnValidResponse() {
    when(service.authenticate(anyString(), anyString())).thenReturn("TOKEN");
    var response = sut.login(loginRequest);
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
  }

  @Test
  void testLogin_whenEntityNotFoundExIsCaught_thenThrowAuthenticationFailedEx() {
    when(service.authenticate(anyString(), anyString())).thenThrow(EntityNotFoundException.class);
    assertThrows(
        AuthenticationFailedException.class,
        () -> sut.login(loginRequest));
  }

  @Test
  void testCreateUser_returnValidResponse() {
    var request = new UserRequest(null, null, null, null, null);
    var user = mapper.toDomain(request);
    user = UserFactory.generateExternalUser(user);
    when(service.createUser(any())).thenReturn(user);
    var response = sut.createUser(request);
    assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
    assertEquals(UserRoleEnum.USER, response.getBody().getRoleName());
  }

}
