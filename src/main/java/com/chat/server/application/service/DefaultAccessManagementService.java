package com.chat.server.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.chat.server.domain.dao.AccessManagementDao;
import com.chat.server.domain.model.User;
import com.chat.server.domain.service.AccessManagementService;
import com.chat.server.domain.service.UserService;
import com.chat.server.infrastructure.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultAccessManagementService implements AccessManagementService {
  
  private final AccessManagementDao dao;
  private final UserService userService;

  @Override
  public String login(String username, char[] password) {
    if (!userService.existsByUsername(username)) {
      String errorMessage = String.format("Username not found: %s", username);
      log.error(errorMessage);
      throw new EntityNotFoundException(errorMessage);
    }

    return dao.login(username, password);
  }

  @Override
  public String createUser(User user) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteUser(UUID userId) {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void updatePassword(User user) {
    // TODO Auto-generated method stub
    
  }

}
