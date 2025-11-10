package com.chat.server.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chat.server.domain.dao.PropertyDao;
import com.chat.server.domain.model.User;
import com.chat.server.domain.util.EncryptUtil;
import com.chat.server.infrastructure.exception.InternalException;

@ExtendWith(MockitoExtension.class)
class DefaultPropertyServiceTest {

  private static final String TEXT = "ANY_TEXT_VALUE";

  @Mock
  private PropertyDao dao;

  @Mock
  private EncryptUtil encryptUtil;

  @InjectMocks
  private DefaultPropertyService sut;

  @Test
  void testGetDefaultInternalUser_returnDefaultInternalUser() throws Exception {
    when(dao.getValueByName(anyString())).thenReturn(TEXT);
    when(encryptUtil.decrypt(anyString())).thenReturn(TEXT);
    var response = sut.getDefaultInternalUser();
    var expectedUser = new User()
        .setUsername(TEXT)
        .setPassword(TEXT)
        .setContacts(new ArrayList<>());
    assertEquals(expectedUser.toString(), response.toString());
  }

  @Test
  void testGetDefaultInternalUser_whenExptionIsCaught_thenThrowInternalException() throws Exception {
    when(dao.getValueByName(anyString())).thenReturn(TEXT);
    when(encryptUtil.decrypt(anyString())).thenThrow(Exception.class);
    assertThrows(
        InternalException.class, 
        () -> sut.getDefaultInternalUser()
    );
  }

}
