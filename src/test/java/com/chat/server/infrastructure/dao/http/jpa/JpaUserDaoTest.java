package com.chat.server.infrastructure.dao.http.jpa;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.chat.server.domain.model.Contact;
import com.chat.server.domain.model.Role;
import com.chat.server.domain.model.User;
import com.chat.server.infrastructure.dao.jpa.JpaUserDao;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.repository.jpa.UserJpaRepository;
import com.chat.server.infrastructure.repository.jpa.entity.ContactEntity;
import com.chat.server.infrastructure.repository.jpa.entity.RoleEntity;
import com.chat.server.infrastructure.repository.jpa.entity.UserEntity;
import com.chat.server.infrastructure.repository.jpa.mapper.UserEntityMapper;

class JpaUserDaoTest {

  @Test
  void testGetById_whenOptionalIsEmpty_throwEntityNotFoundException() {
    var id = UUID.randomUUID();
    UserJpaRepository repository = createRepository(Optional.empty());
    UserEntityMapper mapper = createMapper();
    JpaUserDao sut = new JpaUserDao(repository, mapper);

    var e = assertThrows(
        EntityNotFoundException.class,
        () -> sut.getById(id)
    );
    assertTrue(e.getInternalMessage().contains(id.toString()));
  }

  @Test
  void testGetByUsername_whenOptionalIsEmpty_throwEntityNotFoundException() {
    var username = "USERNAME_TEST";
    UserJpaRepository repository = createRepository(Optional.empty());
    UserEntityMapper mapper = createMapper();
    JpaUserDao sut = new JpaUserDao(repository, mapper);

    var e = assertThrows(
        EntityNotFoundException.class,
        () -> sut.getByUsername(username)
    );
    assertTrue(e.getInternalMessage().contains(username));
  }

  private UserJpaRepository createRepository(Optional<UserEntity> entity) {
    return (UserJpaRepository) Proxy.newProxyInstance(
        UserJpaRepository.class.getClassLoader(),
        new Class<?>[] {UserJpaRepository.class},
        (proxy, method, args) -> {
          switch (method.getName()) {
            case "findById":
              return entity;
            case "getByUsername":
              return entity;
            case "toString":
              return "UserJpaRepository";
            case "hashCode":
              return System.identityHashCode(proxy);
            case "equals":
              return proxy == args[0];
            default:
              return null;
          }
        });
  }

  private UserEntityMapper createMapper() {
    return new UserEntityMapper() {
      @Override
      public UserEntity toEntity(User domain) {
        return new UserEntity();
      }

      @Override
      public User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        return user;
      }

      @Override
      public User toDomainWithoutContacts(UserEntity entity) {
        return toDomain(entity);
      }

      @Override
      public List<User> toDomainWithoutContacts(List<UserEntity> entities) {
        return entities.stream().map(this::toDomainWithoutContacts).toList();
      }
    };
  }

}
