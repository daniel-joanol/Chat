package com.chat.server.infrastructure.controller.apis.internal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import com.chat.server.domain.constants.Constants;
import com.chat.server.domain.model.DirectMessage;
import com.chat.server.domain.service.DirectMessageService;
import com.chat.server.infrastructure.controller.mapper.DirectMessageDtoMapper;
import com.chat.server.infrastructure.controller.request.DirectMessageRequest;
import com.chat.server.infrastructure.exception.BadRequestException;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.exception.ForbiddenException;

@ExtendWith(MockitoExtension.class)
class DirectMessageWebSocketControllerTest {

  private EasyRandomParameters parameters = new EasyRandomParameters().randomizationDepth(2);
  private EasyRandom generator = new EasyRandom(parameters);
  private DirectMessageDtoMapper mapper = Mappers.getMapper(DirectMessageDtoMapper.class);

  @Mock
  private DirectMessageService service;

  @Mock
  private SimpMessagingTemplate messagingTemplate;

  @InjectMocks
  private DirectMessageWebSocketController sut;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(sut, "mapper", mapper);
  }

  @Test
  void testSend_publishMessageToRecipient()
      throws BadRequestException, ForbiddenException, EntityNotFoundException {
    var message = generator.nextObject(DirectMessage.class);
    var request = new DirectMessageRequest(message.getRecipient().getUsername(), message.getContent());
    when(service.send(request.recipientUsername(), request.content())).thenReturn(message);

    sut.send(request);

    verify(messagingTemplate).convertAndSendToUser(
        eq(message.getRecipient().getUsername()),
        eq(Constants.DIRECT_MESSAGE_USER_DESTINATION),
        any());
  }

}