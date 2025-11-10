package com.chat.server.infrastructure.dao.http.jpa;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.chat.server.infrastructure.dao.jpa.JpaUserDao;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.repository.jpa.UserJpaRepository;
import com.chat.server.infrastructure.repository.jpa.mapper.UserEntityMapper;

@ExtendWith(MockitoExtension.class)
class JpaUserDaoTest {
  
  private UserEntityMapper mapper = Mappers.getMapper(UserEntityMapper.class);

  @Mock
  private UserJpaRepository repository;

  @InjectMocks
  private JpaUserDao sut;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(sut, "mapper", mapper);
  }

  @Test
  void testGetById_whenOptionalIsEmpty_throwEntityNotFoundException() {
    var id = UUID.randomUUID();
    when(repository.findById(any())).thenReturn(Optional.empty());
    var e = assertThrows(
        EntityNotFoundException.class, 
        () -> sut.getById(id)
    );
    assertTrue(e.getInternalMessage().contains(id.toString()));
  }

  @Test
  void testGetByUsername_whenOptionalIsEmpty_throwEntityNotFoundException() {
    var username = "USERNAME_TEST";
    when(repository.getByUsername(anyString())).thenReturn(Optional.empty());
    var e = assertThrows(
        EntityNotFoundException.class, 
        () -> sut.getByUsername(username)
    );
    assertTrue(e.getInternalMessage().contains(username));
  }

}
