package com.chat.server.infrastructure.dao.http;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.chat.server.domain.dao.AccessManagementDao;
import com.chat.server.domain.model.TokenInfo;
import com.chat.server.domain.model.User;
import com.chat.server.infrastructure.dao.http.mapper.AccessManagementHttpMapper;
import com.chat.server.infrastructure.dao.http.request.KeycloakFiltersRequest;
import com.chat.server.infrastructure.dao.http.request.KeycloakPasswordRequest;
import com.chat.server.infrastructure.dao.http.request.KeycloakRoleRequest;
import com.chat.server.infrastructure.dao.http.request.KeycloakUserRequest;
import com.chat.server.infrastructure.exception.AuthenticationFailedException;
import com.chat.server.infrastructure.exception.InteralUserForbiddenException;
import com.chat.server.infrastructure.exception.InternalException;
import com.chat.server.infrastructure.repository.http.KeycloakHttpRepository;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AccessManagementHttpDao implements AccessManagementDao {

  private final KeycloakHttpRepository repository;
  private final AccessManagementHttpMapper mapper;

  @Override
  public TokenInfo authenticate(String username, String password) {
    HttpResponse<JsonNode> response = repository.login(username, password)
        .ifFailure(res -> {
          String message = String.format(
              "Error authenticating %s on Keycloak. Petition response: %d, {%s}",
              username, res.getStatus(), res.getBody());
          throw new AuthenticationFailedException(message);
        });
        
    return mapper.toTokenInfo(response.getBody().getObject());
  }

  @Override
  public void deleteUser(String jwt, UUID userId) {
    HttpResponse<JsonNode> response = repository.deleteUser(jwt, userId);
    if (response.getStatus() == 403) {
      String message = String.format("Petition to delete user %s forbidden", userId);
      throw new InteralUserForbiddenException(message);
    
    } else if (!response.isSuccess()) {
      String message = String.format(
          "Error deleting %s from Keycloak. Petition response: %d, {%s}",
          userId, response.getStatus(), response.getBody());
      throw new InternalException(message);
    }
  }

  @Override
  public void createUser(String jwt, User user) {
    KeycloakUserRequest userRequest = mapper.toUserRequest(user);
    HttpResponse<JsonNode> response = repository.createUser(jwt, userRequest);
    if (response.getStatus() == 403) {
      String message = String.format("Petition to create user %s forbidden", user.getUsername());
      throw new InteralUserForbiddenException(message);
    
    } else if (!response.isSuccess()) {
      String message = String.format(
          "Error creating %s on Keycloak. Petition response: %d, {%s}",
          user.getUsername(), response.getStatus(), response.getBody());
      throw new InternalException(message);
    }
  }

  @Override
  public User getUser(String jwt, String username) {
    KeycloakFiltersRequest filtersRequest = KeycloakFiltersRequest.singleUserFilter(username);
    HttpResponse<JsonNode> response = repository.getUser(jwt, filtersRequest);
    if (response.getStatus() == 403) {
      String message = String.format("Petition to get user %s forbidden", username);
      throw new InteralUserForbiddenException(message);
    
    } else if (!response.isSuccess()) {
      String message = String.format(
          "Error getting %s from Keycloak. Petition response: %d, {%s}",
          username, response.getStatus(), response.getBody());
      throw new InternalException(message);
    }

    return mapper.toUser(response.getBody().getArray().getJSONObject(0));
  }

  @Override
  public void addRole(String jwt, User user) {
    KeycloakRoleRequest roleRequest = mapper.toRoleRequest(user.getRole());
    HttpResponse<JsonNode> response = repository.addRoleToUser(jwt, List.of(roleRequest), user.getKeycloakId());
    if (response.getStatus() == 403) {
      String message = String.format("Petition to add role to user %s forbidden", user.getUsername());
      throw new InteralUserForbiddenException(message);
    
    } else if (!response.isSuccess()) {
      String message = String.format(
          "Error adding role %s to user id %s on Keycloak. Petition response: %d, {%s}",
          user.getRole().getName(), user.getKeycloakId(), response.getStatus(), response.getBody());
      throw new InternalException(message);
    }
  }

  @Override
  public void updatePassword(String jwt, User user) {
    KeycloakPasswordRequest passwordRequest = new KeycloakPasswordRequest(user.getPassword());
    HttpResponse<JsonNode> response = repository.updatePassword(jwt, passwordRequest, user.getKeycloakId());
    if (response.getStatus() == 403) {
      String message = String.format("Petition to update password for user %s forbidden", user.getUsername());
      throw new InteralUserForbiddenException(message);
    
    } else if (!response.isSuccess()) {
      String message = String.format(
          "Error updating password for user %s on Keycloak. Petition response: %d, {%s}",
          user.getId(), response.getStatus(), response.getBody());
      throw new InternalException(message);
    }
  }

}
