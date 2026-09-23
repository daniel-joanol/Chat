package com.chat.server.application.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.chat.server.domain.dao.ContactDao;
import com.chat.server.domain.dao.DirectMessageDao;
import com.chat.server.domain.model.User;
import com.chat.server.domain.service.UserService;
import com.chat.server.domain.util.SecurityUtil;
import com.chat.server.infrastructure.exception.BadRequestException;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.exception.ForbiddenException;

@ExtendWith(MockitoExtension.class)
class DefaultDirectMessageServiceTest {

  private EasyRandomParameters parameters = new EasyRandomParameters().randomizationDepth(2);
  private EasyRandom generator = new EasyRandom(parameters);
  private User sender;
  private User recipient;

  @Mock
  private DirectMessageDao dao;

  @Mock
  private ContactDao contactDao;

  @Mock
  private SecurityUtil securityUtil;

  @Mock
  private UserService userService;

  @InjectMocks
  private DefaultDirectMessageService sut;

  @BeforeEach
  void setUp() {
    sender = generator.nextObject(User.class);
    recipient = generator.nextObject(User.class);
  }

  @Test
  void testSend_whenSenderMessagesSelf_thenThrowBadRequestException() {
    when(securityUtil.getUsername()).thenReturn(sender.getUsername());

    assertThrows(
        BadRequestException.class,
        () -> sut.send(sender.getUsername(), "Hello")
    );
  }

  @Test
  void testSend_whenContentIsBlank_thenThrowBadRequestException() {
    when(securityUtil.getUsername()).thenReturn(sender.getUsername());

    assertThrows(
        BadRequestException.class,
        () -> sut.send(recipient.getUsername(), " ")
    );
  }

  @Test
  void testSend_whenRecipientIsNotContact_thenThrowForbiddenException()
      throws EntityNotFoundException {
    when(securityUtil.getUsername()).thenReturn(sender.getUsername());
    when(userService.getByUsername(sender.getUsername())).thenReturn(sender);
    when(userService.getByUsername(recipient.getUsername())).thenReturn(recipient);
    when(contactDao.exists(anyString(), anyString())).thenReturn(false);

    assertThrows(
        ForbiddenException.class,
        () -> sut.send(recipient.getUsername(), "Hello")
    );
  }

  @Test
  void testSend_whenRecipientIsContact_thenSaveMessage()
      throws BadRequestException, ForbiddenException, EntityNotFoundException {
    when(securityUtil.getUsername()).thenReturn(sender.getUsername());
    when(userService.getByUsername(sender.getUsername())).thenReturn(sender);
    when(userService.getByUsername(recipient.getUsername())).thenReturn(recipient);
    when(contactDao.exists(sender.getUsername(), recipient.getUsername())).thenReturn(true);

    sut.send(recipient.getUsername(), "Hello");

    verify(dao).save(any());
  }

}