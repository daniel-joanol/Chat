package com.chat.server.infrastructure.controller.apis.maintenance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chat.server.infrastructure.scheduled.PurgeIncompleteUsersTask;
import com.chat.server.domain.util.SecurityUtil;

@ExtendWith(MockitoExtension.class)
class SchedulerControllerTest {

  @Mock
  private PurgeIncompleteUsersTask purgeIncompleteUsersTask;

  @Mock
  private SecurityUtil securityUtil;

  @InjectMocks
  private SchedulerController sut;

  @Test
  void purgeIncompleteUsers_triggersTaskAndReturnsOk() {
    when(securityUtil.getUsername()).thenReturn("adminUser");

    var resp = sut.purgeIncompleteUsers();

    assertEquals(200, resp.getStatusCodeValue());
    verify(securityUtil).getUsername();
    verify(purgeIncompleteUsersTask).asyncStart("adminUser");
  }

}
