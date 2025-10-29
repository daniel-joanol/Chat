package com.chat.server.infrastructure.dao.http.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.domain.model.Role;
import com.chat.server.domain.model.User;
import com.chat.server.infrastructure.repository.http.request.KeycloakPasswordRequest;
import com.chat.server.infrastructure.repository.http.request.KeycloakRoleRequest;
import com.chat.server.infrastructure.repository.http.request.KeycloakUserRequest;

@Mapper(componentModel = "spring")
public interface AccessManagementHttpMapper {
  
  @Mapping(target = "emailVerified", constant = "true")
  KeycloakUserRequest toUserRequest(User user);

  @Mapping(target = "type", constant = "password")
  @Mapping(target = "temporary", constant = "false")
  KeycloakPasswordRequest toPasswordRequest(char[] password);

  @Mapping(target = "id", source = "keycloakId")
  @Mapping(target = "containerId", source = "clientId")
  @Mapping(target = "description", constant = "")
  @Mapping(target = "name", source = "name", qualifiedByName = "setRoleName")
  KeycloakRoleRequest toRoleRequest(Role role);

  @Named("setRoleName")
  default String setRoleName(UserRoleEnum role) {
    return role.name();
  }

}
