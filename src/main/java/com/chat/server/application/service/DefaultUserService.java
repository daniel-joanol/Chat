package com.chat.server.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.chat.server.domain.dao.AccessManagementDao;
import com.chat.server.domain.dao.UserDao;
import com.chat.server.domain.model.Role;
import com.chat.server.domain.model.User;
import com.chat.server.domain.service.UserService;
import com.chat.server.domain.service.AuthenticationService;
import com.chat.server.domain.service.RoleService;
import com.chat.server.infrastructure.exception.ConflictException;
import com.chat.server.infrastructure.exception.InteralUserForbiddenException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {
  
  private final AccessManagementDao accessManagementDao;
  private final UserDao userDao;
  private final RoleService roleService;
  private final AuthenticationService authService;

  @Override
  public User getByUsername(String username) {
    return userDao.getByUsername(username);
  }

  @Override
  public List<User> getIncompleteUsers() {
    return userDao.getIncompleteUsers();
  }

  @Override
  public void deleteUser(String username) {
    User user = userDao.getByUsername(username);
    this.deleteUser(user);
  }
  
  @Override
  @Transactional
  public void deleteUser(User user) {
    String jwt = authService.getInternalUserJwt(false);
    userDao.delete(user.getId());
    try {
      accessManagementDao.deleteUser(jwt, user.getKeycloakId());
    } catch (InteralUserForbiddenException e) {
      jwt = authService.getInternalUserJwt(true);
      accessManagementDao.deleteUser(jwt, user.getKeycloakId());
    }
  }

  @Override
  public void updatePassword(User user) {
    user = userDao.getById(user.getId());
    String jwt = authService.getInternalUserJwt(false);
    try {
      accessManagementDao.updatePassword(jwt, user);
    } catch (InteralUserForbiddenException e) {
      jwt = authService.getInternalUserJwt(true);
      accessManagementDao.updatePassword(jwt, user);
    }
    
  }

  @Override
  public User createUser(User user) {
    this.validateEmail(user.getEmail());
    this.validateUsername(user.getUsername());
    String jwt = authService.getInternalUserJwt(false);
    String password = user.getPassword();
    Role role = roleService.getByName(user.getRole().getName());
    user.setRole(role);
    
    try {
      accessManagementDao.createUser(jwt, user);
    } catch (InteralUserForbiddenException e) {
      jwt = authService.getInternalUserJwt(true);
      accessManagementDao.createUser(jwt, user);
    }
    
    user = userDao.save(user).setRole(role);
    UUID keycloakId = null;

    try {
      keycloakId = accessManagementDao.getUser(jwt, user.getUsername())
          .getKeycloakId();
    } catch (InteralUserForbiddenException e) {
      jwt = authService.getInternalUserJwt(true);
      keycloakId = accessManagementDao.getUser(jwt, user.getUsername())
          .getKeycloakId();
    }
    
    user.setKeycloakId(keycloakId);
    user = userDao.save(user).setRole(role)
      .setRole(role)
      .setPassword(password);

    try {
      accessManagementDao.addRole(jwt, user);
    } catch (InteralUserForbiddenException e) {
      jwt = authService.getInternalUserJwt(true);
      accessManagementDao.addRole(jwt, user);
    }

    try {
      accessManagementDao.updatePassword(jwt, user);
    } catch (InteralUserForbiddenException exception) {
      jwt = authService.getInternalUserJwt(true);
      accessManagementDao.updatePassword(jwt, user);
    }

    user.setIsCreationCompleted(true);
    return userDao.save(user).setRole(role);
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
