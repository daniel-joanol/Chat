package com.chat.server.infrastructure.dao.http.jpa;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.infrastructure.dao.jpa.JpaRoleDao;
import com.chat.server.infrastructure.exception.InternalException;
import com.chat.server.infrastructure.repository.jpa.RoleJpaRepository;
import com.chat.server.infrastructure.repository.jpa.mapper.RoleEntityMapper;

@ExtendWith(MockitoExtension.class)
class JpaRoleDaoTest {
  
  private RoleEntityMapper mapper = Mappers.getMapper(RoleEntityMapper.class);

  @Mock
  private RoleJpaRepository repository;

  @InjectMocks
  private JpaRoleDao sut;

  @BeforeEach
  void setUp(){
    ReflectionTestUtils.setField(sut, "mapper", mapper);
  }

  @Test
  void testGetByName_whenResponseIsEmpty_thenThrowInternalException() {
    var role = UserRoleEnum.ADMIN;
    when(repository.getByName(any())).thenReturn(Optional.empty());
    var e = assertThrows(
        InternalException.class,
        () -> sut.getByName(role)
    );
    assertTrue(e.getInternalMessage().contains(role.name()));
  }

}
