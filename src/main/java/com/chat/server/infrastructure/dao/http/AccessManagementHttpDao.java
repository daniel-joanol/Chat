package com.chat.server.infrastructure.dao.http;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.chat.server.domain.dao.AccessManagementDao;
import com.chat.server.domain.model.User;
import com.chat.server.infrastructure.dao.http.mapper.AccessManagementHttpMapper;
import com.chat.server.infrastructure.repository.http.KeycloakHttpRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccessManagementHttpDao implements AccessManagementDao {

  private final KeycloakHttpRepository repository;
  private final AccessManagementHttpMapper mapper;

  @Override
  public String login(String username, char[] password) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteUser(String jwt, UUID userId) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String createUser(String jwt, User user) {
    // TODO Auto-generated method stub
    return null;
  }

}
