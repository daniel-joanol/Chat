package com.chat.server.infrastructure.dao.http.jpa;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Proxy;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.chat.server.infrastructure.dao.jpa.JpaPropertyDao;
import com.chat.server.infrastructure.exception.InternalException;
import com.chat.server.infrastructure.repository.jpa.PropertyJpaRepository;

class JpaPropertyDaoTest {

  @Test
  void testGetValueByName_whenResponseIsEmtpy_thenThrowInternalException() {
    String name = "PROP_NAME";
    PropertyJpaRepository repository = createRepository(Optional.empty());
    JpaPropertyDao sut = new JpaPropertyDao(repository);

    var e = assertThrows(
        InternalException.class,
        () -> sut.getValueByName(name));
    assertTrue(e.getInternalMessage().contains("Property not found"));
    assertTrue(e.getInternalMessage().contains(name));
  }

  private PropertyJpaRepository createRepository(Optional<String> value) {
    return (PropertyJpaRepository) Proxy.newProxyInstance(
        PropertyJpaRepository.class.getClassLoader(),
        new Class<?>[] {PropertyJpaRepository.class},
        (proxy, method, args) -> {
          if ("getValueByName".equals(method.getName())) {
            return value;
          }
          if ("toString".equals(method.getName())) {
            return "PropertyJpaRepository";
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
