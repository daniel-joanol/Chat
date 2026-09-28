package com.chat.server.infrastructure.dao.http.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chat.server.domain.model.DirectMessage;
import com.chat.server.infrastructure.dao.jpa.JpaDirectMessageDao;
import com.chat.server.infrastructure.repository.jpa.DirectMessageJpaRepository;
import com.chat.server.infrastructure.repository.jpa.entity.DirectMessageEntity;
import com.chat.server.infrastructure.repository.jpa.mapper.DirectMessageEntityMapper;

@ExtendWith(MockitoExtension.class)
class JpaDirectMessageDaoTest {

  @Mock
  private DirectMessageJpaRepository repository;

  private DirectMessageEntityMapper mapper = Mappers.getMapper(DirectMessageEntityMapper.class);
  private JpaDirectMessageDao sut;

  @BeforeEach
  void setUp() {
    sut = new JpaDirectMessageDao(repository, mapper);
  }

  @Test
  void testSave_returnDirectMessage() {
    var directMessage = new DirectMessage().setId(UUID.randomUUID());
    var savedEntity = new DirectMessageEntity().setId(directMessage.getId());
    when(repository.save(any())).thenReturn(savedEntity);

    var result = sut.save(directMessage);

    assertEquals(directMessage.getId(), result.getId());
  }

}