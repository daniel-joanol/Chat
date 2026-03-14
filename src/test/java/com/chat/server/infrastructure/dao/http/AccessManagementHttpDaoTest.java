package com.chat.server.infrastructure.dao.http;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.function.Consumer;

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

import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.domain.model.Role;
import com.chat.server.domain.model.User;
import com.chat.server.infrastructure.dao.http.mapper.AccessManagementHttpMapper;
import com.chat.server.infrastructure.exception.AuthenticationFailedException;
import com.chat.server.infrastructure.exception.InternalException;
import com.chat.server.infrastructure.repository.http.KeycloakHttpRepository;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;

@ExtendWith(MockitoExtension.class)
class AccessManagementHttpDaoTest {

  private EasyRandomParameters parameters = new EasyRandomParameters()
      .randomizationDepth(2);
  private EasyRandom generator = new EasyRandom(parameters);
  private String username = "username";
  private String password = "pass";
  private String jwt = "JWT";
  private JsonNode errorNode = new JsonNode("{\"message\": \"some error\"}");
  private UUID userId = UUID.randomUUID();
  private User user;
  private Role role;

  private AccessManagementHttpMapper mapper = Mappers.getMapper(AccessManagementHttpMapper.class);
  
  @Mock
  private KeycloakHttpRepository repository;

  @Mock
  private HttpResponse<JsonNode> mockedResponse;

  @InjectMocks
  private AccessManagementHttpDao sut;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(sut, "mapper", mapper);
    role = new Role()
        .setName(UserRoleEnum.USER)
        .setClientId(UUID.randomUUID())
        .setKeycloakId(UUID.randomUUID());
    user = generator.nextObject(User.class)
        .setRole(role);
  }

  @Test
  void testLogin_whenResponseIsSuccess_thenVerifyRepositoryIsCalled() {
    JsonNode node = new JsonNode("{\"access_token\": \"TOKEN\", \"expires_in\": 3600}");

    when(repository.login(anyString(), any())).thenReturn(mockedResponse);
    when(mockedResponse.ifFailure(any())).thenReturn(mockedResponse);
    when(mockedResponse.getBody()).thenReturn(node);

    sut.authenticate(username, password);
    verify(repository).login(anyString(), any());
  }

  @Test
  void testLogin_whenResponseIsFailure_thenThrowException() {
    when(repository.login(anyString(), any())).thenReturn(mockedResponse);
    this.mockLoginFailure();
    
    assertThrows(
        AuthenticationFailedException.class, 
        () -> sut.authenticate(username, password)
    );
  }

  @Test
  void testDeleteUser_whenResponseIsSuccess_thenVerifyRepositoryIsCalled() {
    when(repository.deleteUser(anyString(), any())).thenReturn(mockedResponse);
    mockHttpSuccess();
    sut.deleteUser(jwt, userId);
    verify(repository).deleteUser(anyString(), any());
  }

  @Test
  void testDeleteUser_whenResponseIsFailure_thenThrowException() {
    when(repository.deleteUser(anyString(), any())).thenReturn(mockedResponse);
    mockHttpFailure();
    
    assertThrows(
        InternalException.class, 
        () -> sut.deleteUser(jwt, userId)
    );
  }

  @Test
  void testGetUser_whenResponseIsSuccess_thenVerifyRepositoryIsCalled() {
    JsonNode node = new JsonNode(
        String.format(
            "[{\"id\": \"%s\", \"username\": \"%s\", \"firstName\":\"bla\", \"email\": \"a@com\", \"enabled\": true}]",
            userId, username
    ));

    when(repository.getUser(anyString(), any())).thenReturn(mockedResponse);
    mockHttpSuccess();
    when(mockedResponse.getBody()).thenReturn(node);

    sut.getUser(jwt, username);
    verify(repository).getUser(anyString(), any());
  }

  @Test
  void testGetUser_whenResponseIsFailure_thenThrowException() {
    when(repository.getUser(anyString(), any())).thenReturn(mockedResponse);
    mockHttpFailure();
    
    assertThrows(
        InternalException.class,
        () -> sut.getUser(jwt, username)
    );
  }

  @Test
  void testAddRole_whenResponseIsSuccess_thenVerifyRepositoryIsCalled() {
    when(repository.addRoleToUser(anyString(), any(), any())).thenReturn(mockedResponse);
    mockHttpSuccess();
    sut.addRole(jwt, user);
    verify(repository).addRoleToUser(anyString(), any(), any());
  }

  @Test
  void testAddRole_whenResponseIsFailure_thenThrowException() {
    when(repository.addRoleToUser(anyString(), any(), any())).thenReturn(mockedResponse);
    mockHttpFailure();
    
    assertThrows(
        InternalException.class,
        () -> sut.addRole(jwt, user)
    );
  }

  @Test
  void testUpdatePassword_whenResponseIsSuccess_thenVerifyRepositoryIsCalled() {
    when(repository.updatePassword(anyString(), any(), any())).thenReturn(mockedResponse);
    mockHttpSuccess();
    sut.updatePassword(jwt, user);
    verify(repository).updatePassword(anyString(), any(), any());
  }

  @Test
  void testUpdatePassword_whenResponseIsFailure_thenThrowException() {
    when(repository.updatePassword(anyString(), any(), any())).thenReturn(mockedResponse);
    mockHttpFailure();

    assertThrows(
        InternalException.class,
        () -> sut.updatePassword(jwt, user)
    );
  }

  private void mockHttpSuccess() {
    when(mockedResponse.getStatus()).thenReturn(200);
    when(mockedResponse.isSuccess()).thenReturn(true);
  }

  private void mockHttpFailure() {
    when(mockedResponse.getStatus()).thenReturn(500);
    when(mockedResponse.isSuccess()).thenReturn(false);
    when(mockedResponse.getBody()).thenReturn(errorNode);
  }

  private void mockLoginFailure() {
    when(mockedResponse.ifFailure(any())).thenAnswer(invocation -> {
      Consumer<HttpResponse<JsonNode>> consumer = invocation.getArgument(0);
      consumer.accept(mockedResponse);
      return mockedResponse;
    });
    when(mockedResponse.getStatus()).thenReturn(401);
    when(mockedResponse.getBody()).thenReturn(errorNode);
  }

}
