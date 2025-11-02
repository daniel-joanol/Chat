package com.chat.server.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chat.server.domain.dao.AccessManagementDao;
import com.chat.server.domain.dao.UserDao;
import com.chat.server.domain.model.User;
import com.chat.server.domain.service.UserService;
import com.chat.server.domain.service.PropertyService;
import com.chat.server.infrastructure.exception.ConflictException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
  
  private final AccessManagementDao accessManagementDao;
  private final UserDao userDao;
  private final PropertyService propertyService;

  @Override
  public String authenticate(String username, char[] password) {
    userDao.getByUsername(username);
    return accessManagementDao.authenticate(username, password);
  }

  @Override
  @Transactional
  public void deleteUser(String username) {
    User user = userDao.getByUsername(username);
    String jwt = this.authenticateInternalUser();
    accessManagementDao.deleteUser(jwt, user.getId());
    userDao.delete(user.getId());
  }
  
  @Override
  public void updatePassword(User user) {
    user = userDao.getById(user.getId());
    String jwt = this.authenticateInternalUser();
    accessManagementDao.updatePassword(jwt, user);
  }

  @Override
  public User createUser(User user) {
    this.validateEmail(user.getEmail());
    this.validateUsername(user.getUsername());
    String jwt = this.authenticateInternalUser();
    accessManagementDao.createUser(jwt, user);
    user = userDao.save(user);
    UUID keycloakId = accessManagementDao.getUser(jwt, user.getUsername())
        .getKeycloakId();
    user.setKeycloakId(keycloakId);
    accessManagementDao.addRole(jwt, user);
    accessManagementDao.updatePassword(jwt, user);
    return user.setIsCreationCompleted(true);
  }

  private String authenticateInternalUser() {
    User defaultInternalUser = propertyService.getDefaultInternalUser();
    return this.authenticate(defaultInternalUser.getUsername(), defaultInternalUser.getPassword());
  }

  private void validateEmail(String email) {
    if (userDao.existsByEmail(email)) {
      String message = String.format("Duplicated email: %s", email);
      throw new ConflictException(message);
    }
  }

  private void validateUsername(String username) {
    if (userDao.existsByUsername(username)) {
      String message = String.format("Duplicated username: %s", username);
      throw new ConflictException(message);
    }
  }

}
