package com.chat.server.application.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.chat.server.domain.dao.AccessManagementDao;
import com.chat.server.domain.model.TokenInfo;
import com.chat.server.domain.model.User;
import com.chat.server.domain.service.PropertyService;
import com.chat.server.domain.service.UserService;

@ExtendWith(MockitoExtension.class)
class DefaultAuthenticationServiceTest {

  TokenInfo tokenInfo = new TokenInfo("TOKEN", LocalDateTime.now());
  User user = new User()
      .setUsername("username")
      .setPassword("password");
  
  @Mock
  AccessManagementDao accessManagementDao;

  @Mock
  PropertyService propertyService;

  @Mock
  UserService userService;

  @InjectMocks
  DefaultAuthenticationService sut;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(sut, "internalUserJwt", "TOKEN");
    ReflectionTestUtils.setField(sut, "expirationDate", LocalDateTime.now().minusMinutes(10l));
  }

  @Test
  void testAuthenticate_returnToken() {
    when(accessManagementDao.authenticate(anyString(), anyString())).thenReturn(tokenInfo);
    var response = sut.authenticate("username", "password");
    assertNotNull(response);
  }

  @Test
  void testGetInternalUserJwt_returnToken() {
    when(propertyService.getDefaultInternalUser()).thenReturn(user);
    when(accessManagementDao.authenticate(anyString(), anyString())).thenReturn(tokenInfo);
    var response = sut.getInternalUserJwt(false);
    assertNotNull(response);
  }

}
