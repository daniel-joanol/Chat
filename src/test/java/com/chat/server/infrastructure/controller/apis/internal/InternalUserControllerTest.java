package com.chat.server.infrastructure.controller.apis.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.chat.server.domain.service.UserService;
import com.chat.server.domain.util.SecurityUtil;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.exception.InternalException;

@ExtendWith(MockitoExtension.class)
class InternalUserControllerTest {

  @Mock
  private UserService userService;

  @Mock
  private SecurityUtil securityUtil;

  @InjectMocks
  private InternalUserController sut;

  @Test
  void testLogout_markCurrentUserOffline() throws InternalException, EntityNotFoundException {
    var username = "alice";
    when(securityUtil.getUsername()).thenReturn(username);

    var response = sut.logout();

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(userService).logout(username);
  }

}