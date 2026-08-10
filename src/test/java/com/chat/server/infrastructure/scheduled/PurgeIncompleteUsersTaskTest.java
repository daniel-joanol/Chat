package com.chat.server.infrastructure.scheduled;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chat.server.domain.model.User;
import com.chat.server.domain.service.UserService;
import com.chat.server.infrastructure.exception.AuthenticationFailedException;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.exception.InternalUserForbiddenException;

@ExtendWith(MockitoExtension.class)
class PurgeIncompleteUsersTaskTest {

  private EasyRandom generator = new EasyRandom(new EasyRandomParameters().randomizationDepth(1));

  @Mock
  private UserService userService;

  @InjectMocks
  private PurgeIncompleteUsersTask sut;

  private User user1;
  private User user2;

  @BeforeEach
  void setUp() {
    user1 = generator.nextObject(User.class);
    user2 = generator.nextObject(User.class);
    user1.setUsername("u1");
    user2.setUsername("u2");
  }

  @Test
  void purge_purgesSuccessfulUsersAndContinuesOnFailure() throws EntityNotFoundException, InternalUserForbiddenException, AuthenticationFailedException {
    when(userService.getIncompleteUsers()).thenReturn(List.of(user1, user2));
    doThrow(new RuntimeException("fail")).when(userService).deleteUser(user1);

    int purged = sut.purge();

    assertEquals(1, purged);
  }

}
