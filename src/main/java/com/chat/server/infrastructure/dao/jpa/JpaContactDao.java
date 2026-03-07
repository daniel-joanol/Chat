package com.chat.server.infrastructure.dao.jpa;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.chat.server.domain.dao.ContactDao;
import com.chat.server.domain.model.Contact;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.repository.jpa.ContactJpaRepository;
import com.chat.server.infrastructure.repository.jpa.mapper.ContactEntityMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JpaContactDao implements ContactDao {
  
  private final ContactJpaRepository repository;
  private final ContactEntityMapper mapper;
  
  @Override
  public void delete(UUID id) {
    repository.deleteById(id);
  }
  @Override
  public boolean exists(String userUsername, String contactUsername) {
    return repository.exists(userUsername, contactUsername);
  }
  
  @Override
  public Contact getById(UUID id) {
    return repository.findById(id)
        .map(mapper::toDomain)
        .orElseThrow(() -> {
          var message = String.format("Contact not found: %s", id);
          throw new EntityNotFoundException(message);
        });
  }
  
  @Override
  public Contact save(Contact contact) {
    var entity = mapper.toEntity(contact);
    entity = repository.save(entity);
    return mapper.toDomain(entity);
  }

}
