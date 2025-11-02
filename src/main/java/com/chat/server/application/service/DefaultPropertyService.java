package com.chat.server.application.service;

import org.springframework.stereotype.Service;

import com.chat.server.domain.constants.Constants;
import com.chat.server.domain.dao.PropertyDao;
import com.chat.server.domain.model.User;
import com.chat.server.domain.model.UserFactory;
import com.chat.server.domain.service.PropertyService;
import com.chat.server.domain.util.EncryptUtil;
import com.chat.server.infrastructure.exception.InternalException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultPropertyService implements PropertyService {
  
  private final PropertyDao dao;
  private final EncryptUtil encryptUtil;

  @Override
  public User getDefaultInternalUser() {
    String username = this.getDefaultInternalUsername();
    char[] pass = this.getDefaultInternalUserPass();
    return UserFactory.generateDefaultInternalUser(username, pass);
  }

  private String getDefaultInternalUsername() {
    return dao.getValueByName(Constants.PROPERTY_DEFAULT_KEYCLOAK_USER);
  }

  private char[] getDefaultInternalUserPass() {
    char[] encryptedPass = dao.getPasswordByName(Constants.PROPERTY_DEFAULT_KEYCLOAk_USER_PASS);
    try {
      return encryptUtil.decrypt(encryptedPass);
    
    } catch (Exception e) {
      String message = "Error decrypting password for default internal user.";
      throw new InternalException(message);
    }
  }

}
