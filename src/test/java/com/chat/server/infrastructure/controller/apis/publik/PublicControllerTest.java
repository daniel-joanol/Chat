package com.chat.server.infrastructure.controller.apis.publik;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.domain.model.UserFactory;
import com.chat.server.domain.service.UserService;
import com.chat.server.domain.util.SecurityUtil;
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
  private UserService userService;

  @Mock
  private SecurityUtil securityUtil;

  @InjectMocks
  private PublicController sut;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(sut, "mapper", mapper);
  }

  @Test
  void testLogin_returnValidResponse() {
    when(userService.authenticate(anyString(), anyString())).thenReturn("TOKEN");
    var response = sut.login(loginRequest);
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
  }

  @Test
  void testLogin_whenEntityNotFoundExIsCaught_thenThrowAuthenticationFailedEx() {
    when(userService.authenticate(anyString(), anyString())).thenThrow(EntityNotFoundException.class);
    assertThrows(
        AuthenticationFailedException.class,
        () -> sut.login(loginRequest));
  }

  @Test
  void testCreateUser_returnValidResponse() {
    var request = new UserRequest(null, null, null, null, null);
    var user = mapper.toDomain(request);
    user = UserFactory.generateExternalUser(user);
    when(userService.createUser(any())).thenReturn(user);
    var response = sut.createUser(request);
    assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
    assertEquals(UserRoleEnum.USER, response.getBody().getRoleName());
  }

  @Test
  void logout_callsServiceAndReturnsNoContent() {
    when(securityUtil.getUsername()).thenReturn("testuser");

    ResponseEntity<Void> resp = sut.logout();

    assertEquals(204, resp.getStatusCodeValue());
    verify(securityUtil).getUsername();
    verify(userService).logout("testuser");
  }

}
