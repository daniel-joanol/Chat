package com.chat.server.application.service;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.chat.server.domain.dao.AccessManagementDao;
import com.chat.server.domain.model.User;
import com.chat.server.domain.service.AuthenticationService;
import com.chat.server.domain.service.PropertyService;

@Service
public class DefaultAuthenticationService implements AuthenticationService {

  private String internalUserJwt = null;
  private LocalDateTime expirationDate = null;

  private final AccessManagementDao accessManagementDao;
  private final PropertyService propertyService;
  private final DefaultUserService userService;

  public DefaultAuthenticationService(
      AccessManagementDao accessManagementDao,
      PropertyService propertyService,
      @Lazy DefaultUserService userService
  ) {
    this.accessManagementDao = accessManagementDao;
    this.propertyService = propertyService;
    this.userService = userService;
  }

  @Override
  public String authenticate(String username, String password) {
    userService.getByUsername(username);
    return accessManagementDao.authenticate(username, password);
  }

  @Override
  public String getInternalUserJwt() {
    boolean authenticate = internalUserJwt == null || expirationDate == null || LocalDateTime.now().isAfter(expirationDate.minusMinutes(2l));
    if (authenticate) {
      internalUserJwt = this.authenticateInternalUser();
      expirationDate = LocalDateTime.now().plusMinutes(10);
    }

    return internalUserJwt;
  }

  private String authenticateInternalUser() {
    User defaultInternalUser = propertyService.getDefaultInternalUser();
    return accessManagementDao.authenticate(defaultInternalUser.getUsername(), defaultInternalUser.getPassword());
  }
  
}
