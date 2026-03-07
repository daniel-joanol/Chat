package com.chat.server.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chat.server.domain.dao.ContactDao;
import com.chat.server.domain.model.Contact;
import com.chat.server.domain.model.User;
import com.chat.server.domain.service.UserService;
import com.chat.server.domain.util.SecurityUtil;
import com.chat.server.infrastructure.exception.BadRequestException;
import com.chat.server.infrastructure.exception.ConflictException;
import com.chat.server.infrastructure.exception.ForbiddenException;

@ExtendWith(MockitoExtension.class)
class DefaultContactServiceTest {
  
  private EasyRandomParameters parameters = new EasyRandomParameters()
      .randomizationDepth(2);
  private EasyRandom generator = new EasyRandom(parameters);
  private User user;
  private Contact contact;
  private UUID id = UUID.randomUUID();

  @Mock
  private ContactDao dao;

  @Mock
  private SecurityUtil secUtil;

  @Mock
  private UserService userService;

  @InjectMocks
  private DefaultContactService sut;

  @BeforeEach
  void setUp() {
    user = generator.nextObject(User.class);
    contact = generator.nextObject(Contact.class);
  }

  @Test
  void testAddContact_whenUserAddingSelf_thenThrowBadRequestEx() {
    when(secUtil.getUsername()).thenReturn(user.getUsername());
    assertThrows(
        BadRequestException.class,
        () -> sut.addContact(user.getUsername())
    );
  }

  @Test
  void testAddContact_whenContactExists_thenThrowConflictEx() {
    when(secUtil.getUsername()).thenReturn(user.getUsername());
    when(dao.exists(anyString(), anyString())).thenReturn(true);
    assertThrows(
        ConflictException.class, 
      () -> sut.addContact(contact.getFriend().getUsername())
    );
  }

  @Test
  void testAddContact_verifyDaoIsCalled() {
    when(secUtil.getUsername()).thenReturn(user.getUsername());
    when(dao.exists(anyString(), anyString())).thenReturn(false);
    when(userService.getByUsername(anyString())).thenReturn(user);
    sut.addContact(contact.getFriend().getUsername());
    verify(dao).save(any());
  }

  @Test
  void testDelete_whenContactBelongsToAnotherUser_thenThrowForbiddenEx() {
    when(secUtil.getUsername()).thenReturn(user.getUsername());
    when(dao.getById(any())).thenReturn(contact);
    assertThrows(
        ForbiddenException.class, 
        () -> sut.delete(id)
    );
  }

  @Test
  void testDelete_verifyDaoIsCalled() {
    when(secUtil.getUsername()).thenReturn(contact.getUser().getUsername());
    when(dao.getById(any())).thenReturn(contact);
    sut.delete(id);
    verify(dao).delete(any());
  }

}
