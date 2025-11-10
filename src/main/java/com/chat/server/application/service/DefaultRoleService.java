package com.chat.server.application.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.chat.server.domain.dao.RoleDao;
import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.domain.model.Role;
import com.chat.server.domain.service.RoleService;


@Service
public class DefaultRoleService implements RoleService {
  
  private final RoleDao dao;
  private final UUID clientId;

  public DefaultRoleService(
      RoleDao dao,
      @Value("${keycloak.client.container.id}") UUID cliendId
  ) {
    this.dao = dao;
    this.clientId = cliendId;
  }

  @Override
  public Role getByName(UserRoleEnum name) {
    return dao.getByName(name)
        .setClientId(clientId);
  }

}
