package com.chat.server.infrastructure.dao.jpa;

import org.springframework.stereotype.Component;

import com.chat.server.domain.dao.DirectMessageDao;
import com.chat.server.domain.model.DirectMessage;
import com.chat.server.infrastructure.repository.jpa.DirectMessageJpaRepository;
import com.chat.server.infrastructure.repository.jpa.mapper.DirectMessageEntityMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JpaDirectMessageDao implements DirectMessageDao {

  private final DirectMessageJpaRepository repository;
  private final DirectMessageEntityMapper mapper;

  @Override
  public DirectMessage save(DirectMessage message) {
    var entity = mapper.toEntity(message);
    entity = repository.save(entity);
    return mapper.toDomain(entity);
  }

}