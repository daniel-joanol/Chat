package com.chat.server.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import com.chat.server.domain.service.AuthenticationService;
import com.chat.server.domain.service.PropertyService;
import com.chat.server.domain.service.RoleService;
import com.chat.server.infrastructure.exception.ConflictException;
import com.chat.server.infrastructure.exception.InternalUserForbiddenException;

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
  private AuthenticationService authService;

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
  void testDeleteUser_givenForbiddenEx_thenRetryWithAdminJwt() {
    when(userDao.getByUsername(anyString())).thenReturn(user);
    when(authService.getInternalUserJwt(anyBoolean())).thenReturn("TOKEN");
    doThrow(new InternalUserForbiddenException("Forbidden"))
        .doNothing()
        .when(accessManagementDao).deleteUser(anyString(), any());
    sut.deleteUser(user.getUsername());
    verify(accessManagementDao, times(2)).deleteUser(anyString(), any());
  }

  @Test
  void testUpdatePassword_givenForbiddenEx_thenRetryWithAdminJwt() {
    when(userDao.getById(any())).thenReturn(user);
    when(authService.getInternalUserJwt(anyBoolean())).thenReturn("TOKEN");
    doThrow(new InternalUserForbiddenException("Forbidden"))
        .doNothing()
        .when(accessManagementDao).updatePassword(anyString(), any());
    sut.updatePassword(user);
    verify(accessManagementDao, times(2)).updatePassword(anyString(), any());
  }

  @Test
  void testCreateUser_givenForbiddenEx_thenReturnExternalUser() {    
    when(userDao.existsByEmail(anyString())).thenReturn(false);
    when(userDao.existsByUsername(anyString())).thenReturn(false);
    when(authService.getInternalUserJwt(anyBoolean())).thenReturn("TOKEN");
    when(roleService.getByName(any())).thenReturn(role);
    doThrow(new InternalUserForbiddenException("Forbidden"))
        .doNothing()
        .when(accessManagementDao).createUser(anyString(), any());
    when(accessManagementDao.getUser(anyString(), anyString()))
        .thenThrow(new InternalUserForbiddenException("Forbidden"))
        .thenReturn(user);
    doThrow(new InternalUserForbiddenException("Forbidden"))
        .doNothing()
        .when(accessManagementDao).addRole(anyString(), any());
    doThrow(new InternalUserForbiddenException("Forbidden"))
        .doNothing()
        .when(accessManagementDao).updatePassword(anyString(), any());
    when(userDao.save(any())).thenReturn(user);

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
