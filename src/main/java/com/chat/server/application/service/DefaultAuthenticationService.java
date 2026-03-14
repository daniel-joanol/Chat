package com.chat.server.application.service;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.chat.server.domain.dao.AccessManagementDao;
import com.chat.server.domain.model.TokenInfo;
import com.chat.server.domain.model.User;
import com.chat.server.domain.service.AuthenticationService;
import com.chat.server.domain.service.PropertyService;
import com.chat.server.domain.service.UserService;

@Service
public class DefaultAuthenticationService implements AuthenticationService {

  private String internalUserJwt = null;
  private LocalDateTime expirationDate = null;

  private final AccessManagementDao accessManagementDao;
  private final PropertyService propertyService;
  private final UserService userService;

  public DefaultAuthenticationService(
      AccessManagementDao accessManagementDao,
      PropertyService propertyService,
      @Lazy UserService userService
  ) {
    this.accessManagementDao = accessManagementDao;
    this.propertyService = propertyService;
    this.userService = userService;
  }

  @Override
  public String authenticate(String username, String password) {
    userService.getByUsername(username);
    TokenInfo tokenInfo = accessManagementDao.authenticate(username, password);
    return tokenInfo.accessToken();
  }

  @Override
  public String getInternalUserJwt(boolean forceAuthentication) {
    boolean authenticate = forceAuthentication || internalUserJwt == null || expirationDate == null || LocalDateTime.now().isAfter(expirationDate.minusMinutes(2l));
    if (authenticate) {
      TokenInfo tokenInfo = this.authenticateInternalUser();
      internalUserJwt = tokenInfo.accessToken();
      expirationDate = tokenInfo.expiresAt();
    }

    return internalUserJwt;
  }

  private TokenInfo authenticateInternalUser() {
    User defaultInternalUser = propertyService.getDefaultInternalUser();
    return accessManagementDao.authenticate(defaultInternalUser.getUsername(), defaultInternalUser.getPassword());
  }
  
}
