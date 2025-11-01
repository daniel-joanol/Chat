package com.chat.server.infrastructure.dao.http.mapper;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.domain.model.Role;
import com.chat.server.domain.model.User;
import com.chat.server.infrastructure.dao.http.request.KeycloakRoleRequest;
import com.chat.server.infrastructure.dao.http.request.KeycloakUserRequest;

import kong.unirest.core.json.JSONObject;

@Mapper(componentModel = "spring")
public interface AccessManagementHttpMapper {
  
  @Mapping(target = "emailVerified", constant = "true")
  @Mapping(target = "enabled", constant = "true")
  KeycloakUserRequest toUserRequest(User user);

  @Mapping(target = "id", source = "keycloakId")
  @Mapping(target = "containerId", source = "clientId")
  @Mapping(target = "name", source = "name", qualifiedByName = "setRoleName")
  KeycloakRoleRequest toRoleRequest(Role role);

  @Named("setRoleName")
  default String setRoleName(UserRoleEnum role) {
    return role.name();
  }

  default User toUser(JSONObject json) {
    return new User()
        .setId(UUID.fromString(json.getString("id")))
        .setUsername(json.getString("username"))
        .setFirstName(json.getString("firstName"))
        .setLastName(json.has("lastName") ? json.getString("lastName") : "")
        .setEmail(json.getString("email"))
        .setIsEnabled(json.getBoolean("enabled"));
  }

}
