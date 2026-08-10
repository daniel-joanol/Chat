package com.chat.server.infrastructure.controller.apis.publik;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.chat.server.domain.service.UserService;
import com.chat.server.domain.util.SecurityUtil;
import com.chat.server.infrastructure.controller.mapper.UserDtoMapper;

@ExtendWith(MockitoExtension.class)
class PublicControllerTest {

  @Mock
  private UserService userService;

  @Mock
  private UserDtoMapper mapper;

  @Mock
  private SecurityUtil securityUtil;

  @InjectMocks
  private PublicController sut;

  @BeforeEach
  void setUp() {
  }

  @Test
  void logout_callsServiceAndReturnsNoContent() {
    when(securityUtil.getUsername()).thenReturn("testuser");

    ResponseEntity<Void> resp = sut.logout();

    assertEquals(204, resp.getStatusCode());
    verify(securityUtil).getUsername();
    verify(userService).logout("testuser");
  }

}
