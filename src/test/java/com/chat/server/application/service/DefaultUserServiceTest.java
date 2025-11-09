package com.chat.server.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chat.server.domain.dao.AccessManagementDao;
import com.chat.server.domain.dao.UserDao;
import com.chat.server.domain.model.Role;
import com.chat.server.domain.model.User;
import com.chat.server.domain.service.PropertyService;
import com.chat.server.domain.service.RoleService;
import com.chat.server.infrastructure.exception.ConflictException;

@ExtendWith(MockitoExtension.class)
class DefaultUserServiceTest {

  private EasyRandomParameters parameters = new EasyRandomParameters()
      .randomizationDepth(2);
  private EasyRandom generator = new EasyRandom(parameters);
  private User user;
  private Role role;
  
  @Mock
  private AccessManagementDao accessManagementDao;

  @Mock
  private UserDao userDao;

  @Mock
  private PropertyService propertyService;

  @Mock
  private RoleService roleService;

  @InjectMocks
  private DefaultUserService sut;

  @BeforeEach
  void setUp() {
    user = generator.nextObject(User.class);
    role = generator.nextObject(Role.class);
  }

  @Test
  void testCreateUser_returnExternalUser() {    
    when(userDao.existsByEmail(anyString())).thenReturn(false);
    when(userDao.existsByUsername(anyString())).thenReturn(false);
    when(propertyService.getDefaultInternalUser()).thenReturn(user);
    when(accessManagementDao.authenticate(anyString(), anyString())).thenReturn("TOKEN");
    when(roleService.getByName(any())).thenReturn(role);
    when(userDao.save(any())).thenReturn(user);
    when(accessManagementDao.getUser(anyString(), anyString())).thenReturn(user);

    var response = sut.createUser(user);
    assertEquals(role.toString(), response.getRole().toString());
  }

  @Test
  void testCreateUser_givenDuplicatedEmail_thenThrowConflictException() {
    when(userDao.existsByEmail(anyString())).thenReturn(true);
    assertThrows(
        ConflictException.class,
        () -> sut.createUser(user));
  }

  @Test
  void testCreateUser_givenDuplicatedPassword_thenThrowConflictException() {
    when(userDao.existsByEmail(anyString())).thenReturn(false);
    when(userDao.existsByUsername(anyString())).thenReturn(true);
    assertThrows(
        ConflictException.class,
        () -> sut.createUser(user));
  }

}
