package com.chat.server.infrastructure.dao;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.chat.server.infrastructure.dao.http.AccessManagementHttpDao;
import com.chat.server.infrastructure.dao.http.mapper.AccessManagementHttpMapper;
import com.chat.server.infrastructure.exception.AuthenticationFailedException;
import com.chat.server.infrastructure.repository.http.KeycloakHttpRepository;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;

@ExtendWith(MockitoExtension.class)
class AccessManagementHttpDaoTest {

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
  }

  @Test
  void testLogin_whenResponseIsSuccess_thenVerifyRepositoryIsCalled() {
    JsonNode node = new JsonNode("{\"access_token\": \"TOKEN\"}");
    
    when(repository.login(anyString(), any())).thenReturn(mockedResponse);
    when(mockedResponse.isSuccess()).thenReturn(true);
    when(mockedResponse.getBody()).thenReturn(node);
    when(mockedResponse.getStatus()).thenReturn(403);

    sut.login("username", new char[]{'p','a','s','s'});
    verify(repository).login(anyString(), any());
  }

  @Test
  void testLogin_whenReponseIsFailure_thenThrowException() {
    JsonNode node = new JsonNode("{\"access_token\": \"TOKEN\"}");
    
    when(repository.login(anyString(), any())).thenReturn(mockedResponse);
    when(mockedResponse.isSuccess()).thenReturn(false);
    when(mockedResponse.getStatus()).thenReturn(403);
    when(mockedResponse.getBody()).thenReturn(node);

    assertThrows(
        AuthenticationFailedException.class, 
        () -> sut.login("username", new char[]{'p','a','s','s'})
    );
  }

}
