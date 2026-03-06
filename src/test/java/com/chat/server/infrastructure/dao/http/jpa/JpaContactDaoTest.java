package com.chat.server.infrastructure.dao.http.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.chat.server.infrastructure.dao.jpa.JpaContactDao;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.repository.jpa.ContactJpaRepository;
import com.chat.server.infrastructure.repository.jpa.entity.ContactEntity;
import com.chat.server.infrastructure.repository.jpa.mapper.ContactEntityMapper;

@ExtendWith(MockitoExtension.class)
class JpaContactDaoTest {

  EasyRandomParameters parameters = new EasyRandomParameters()
      .randomizationDepth(3);
  EasyRandom generator = new EasyRandom(parameters);
  ContactEntityMapper mapper = Mappers.getMapper(ContactEntityMapper.class);
  ContactEntity entity;
  
  @Mock
  ContactJpaRepository repository;

  @InjectMocks
  JpaContactDao sut;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(sut, "mapper", mapper);
    entity = generator.nextObject(ContactEntity.class);
  }

  @Test
  void testGetById_returnContact() {
    when(repository.findById(any())).thenReturn(Optional.of(entity));
    var contact = sut.getById(entity.getId());
    assertEquals(entity.getId(), contact.getId());
  }

  @Test
  void testGetById_whenIdNotFound_thenThrowException() {
    when(repository.findById(any())).thenReturn(Optional.empty());
    var id = entity.getId();
    assertThrows(
        EntityNotFoundException.class, 
        () -> sut.getById(id)
    );
  }

  @Test
  void testSave_returnContact() {
    var domain = mapper.toDomain(entity);
    when(repository.save(any())).thenReturn(entity);
    var response = sut.save(domain);
    assertEquals(domain.getId(), response.getId());
  }

}
