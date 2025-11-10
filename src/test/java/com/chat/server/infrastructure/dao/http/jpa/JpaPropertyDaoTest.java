package com.chat.server.infrastructure.dao.http.jpa;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.chat.server.infrastructure.dao.jpa.JpaPropertyDao;
import com.chat.server.infrastructure.exception.InternalException;
import com.chat.server.infrastructure.repository.jpa.PropertyJpaRepository;

@ExtendWith(MockitoExtension.class)
class JpaPropertyDaoTest {
  
  @Mock
  private PropertyJpaRepository repository;

  @InjectMocks
  private JpaPropertyDao sut;

  @Test
  void testGetValueByName_whenResponseIsEmtpy_thenThrowInternalException() {
    String name = "PROP_NAME";
    when(repository.getValueByName(anyString())).thenReturn(Optional.empty());
    var e = assertThrows(
        InternalException.class,
        () -> sut.getValueByName(name));
    assertTrue(e.getInternalMessage().contains(name));
  }

}
