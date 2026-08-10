package com.chat.server.infrastructure.dao.http.jpa;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.domain.model.Role;
import com.chat.server.infrastructure.dao.jpa.JpaRoleDao;
import com.chat.server.infrastructure.exception.InternalException;
import com.chat.server.infrastructure.repository.jpa.RoleJpaRepository;
import com.chat.server.infrastructure.repository.jpa.entity.RoleEntity;
import com.chat.server.infrastructure.repository.jpa.mapper.RoleEntityMapper;

class JpaRoleDaoTest {

  @Test
  void testGetByName_whenResponseIsEmpty_thenThrowInternalException() {
    var role = UserRoleEnum.ADMIN;
    RoleJpaRepository repository = createRepository(Optional.empty());
    RoleEntityMapper mapper = new RoleEntityMapper() {
      @Override
      public Role toDomain(RoleEntity entity) {
        return new Role();
      }

      @Override
      public RoleEntity toEntity(Role domain) {
        return new RoleEntity();
      }
    };
    JpaRoleDao sut = new JpaRoleDao(repository, mapper);

    var e = assertThrows(
        InternalException.class,
        () -> sut.getByName(role)
    );
    assertTrue(e.getInternalMessage().contains("Role not found"));
    assertTrue(e.getInternalMessage().contains(role.name()));
  }

  private RoleJpaRepository createRepository(Optional<RoleEntity> value) {
    return (RoleJpaRepository) Proxy.newProxyInstance(
        RoleJpaRepository.class.getClassLoader(),
        new Class<?>[] {RoleJpaRepository.class},
        (proxy, method, args) -> {
          if ("getByName".equals(method.getName())) {
            return value;
          }
          if ("toString".equals(method.getName())) {
            return "RoleJpaRepository";
          }
          if ("hashCode".equals(method.getName())) {
            return System.identityHashCode(proxy);
          }
          if ("equals".equals(method.getName())) {
            return proxy == args[0];
          }
          return null;
        });
  }

}
