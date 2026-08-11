package com.chat.server.infrastructure.dao.http.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.chat.server.domain.model.Contact;
import com.chat.server.infrastructure.dao.jpa.JpaContactDao;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.repository.jpa.ContactJpaRepository;
import com.chat.server.infrastructure.repository.jpa.entity.ContactEntity;
import com.chat.server.infrastructure.repository.jpa.mapper.ContactEntityMapper;

class JpaContactDaoTest {

  EasyRandomParameters parameters = new EasyRandomParameters()
      .randomizationDepth(3);
  EasyRandom generator = new EasyRandom(parameters);
  ContactEntityMapper mapper = new ContactEntityMapper() {
    @Override
    public ContactEntity toEntity(Contact domain) {
      ContactEntity entity = new ContactEntity();
      entity.setId(domain.getId());
      return entity;
    }

    @Override
    public Contact toDomain(ContactEntity entity) {
      Contact domain = new Contact();
      domain.setId(entity.getId());
      return domain;
    }

    @Override
    public List<Contact> toDomain(List<ContactEntity> entities) {
      return entities.stream().map(this::toDomain).toList();
    }
  };
  ContactEntity entity;
  JpaContactDao sut;

  @BeforeEach
  void setUp() {
    entity = generator.nextObject(ContactEntity.class);
    ContactJpaRepository repository = createRepository(Optional.of(entity));
    sut = new JpaContactDao(repository, mapper);
  }

  @Test
  void testGetById_returnContact() throws EntityNotFoundException {
    var contact = sut.getById(entity.getId());
    assertEquals(entity.getId(), contact.getId());
  }

  @Test
  void testGetById_whenIdNotFound_thenThrowException() {
    ContactJpaRepository repository = createRepository(Optional.empty());
    JpaContactDao dao = new JpaContactDao(repository, mapper);
    var id = UUID.randomUUID();
    assertThrows(
        EntityNotFoundException.class,
        () -> dao.getById(id)
    );
  }

  @Test
  void testSave_returnContact() {
    var domain = mapper.toDomain(entity);
    var response = sut.save(domain);
    assertEquals(domain.getId(), response.getId());
  }

  private ContactJpaRepository createRepository(Optional<ContactEntity> entity) {
    return (ContactJpaRepository) Proxy.newProxyInstance(
        ContactJpaRepository.class.getClassLoader(),
        new Class<?>[] {ContactJpaRepository.class},
        (proxy, method, args) -> {
          switch (method.getName()) {
            case "findById":
              return entity;
            case "save":
              return entity.get();
            case "toString":
              return "ContactJpaRepository";
            case "hashCode":
              return System.identityHashCode(proxy);
            case "equals":
              return proxy == args[0];
            default:
              return null;
          }
        });
  }

}
