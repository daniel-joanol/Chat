package com.chat.server.infrastructure.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import com.chat.server.domain.model.Contact;
import com.chat.server.domain.service.ContactService;
import com.chat.server.infrastructure.controller.apis.internal.ContactController;
import com.chat.server.infrastructure.controller.mapper.ContactDtoMapper;
import com.chat.server.infrastructure.controller.request.ContactRequest;

@ExtendWith(MockitoExtension.class)
class ContactControllerTest {
  
  EasyRandomParameters parameters = new EasyRandomParameters()
      .randomizationDepth(3);
  EasyRandom generator = new EasyRandom(parameters);
  ContactDtoMapper mapper = Mappers.getMapper(ContactDtoMapper.class);

  @Mock
  ContactService service;

  @InjectMocks
  ContactController sut;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(sut, "mapper", mapper);
  }

  @Test
  void testAdd_returnCreated() {
    var contact = generator.nextObject(Contact.class);
    var request = new ContactRequest(contact.getFriend().getUsername());
    when(service.addContact(any())).thenReturn(contact);
    var response = sut.add(request);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Test
  void testDelete_returnNoContent() {
    var id = UUID.randomUUID();
    var response = sut.delete(id);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

}
