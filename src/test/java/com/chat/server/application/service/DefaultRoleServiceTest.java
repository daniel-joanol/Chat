package com.chat.server.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chat.server.domain.dao.RoleDao;
import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.domain.model.Role;

@ExtendWith(MockitoExtension.class)
class DefaultRoleServiceTest {

  private UUID clientId;
  
  @Mock
  private RoleDao dao;

  private DefaultRoleService sut;

  @BeforeEach
  void setUp() {
    clientId = UUID.randomUUID();
    sut = new DefaultRoleService(dao, clientId);
  }

  @Test
  void testGetByName_returnRoleWithClientId() {
    when(dao.getByName(any())).thenReturn(new Role());
    var role = sut.getByName(UserRoleEnum.USER);
    assertEquals(clientId, role.getClientId());
  }

}
