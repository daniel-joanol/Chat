package com.chat.server.infrastructure.dao.http;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.chat.server.domain.dao.AccessManagementDao;
import com.chat.server.domain.model.User;
import com.chat.server.infrastructure.dao.http.mapper.AccessManagementHttpMapper;
import com.chat.server.infrastructure.dao.http.request.KeycloakFiltersRequest;
import com.chat.server.infrastructure.dao.http.request.KeycloakPasswordRequest;
import com.chat.server.infrastructure.dao.http.request.KeycloakRoleRequest;
import com.chat.server.infrastructure.dao.http.request.KeycloakUserRequest;
import com.chat.server.infrastructure.exception.AuthenticationFailedException;
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
  public String login(String username, char[] password) {
    HttpResponse<JsonNode> response = repository.login(username, password)
        .ifFailure(res -> {
          String message = String.format(
              "Error authenticating %s on Keycloak. Petition response: %d, {%s}",
              username, res.getStatus(), res.getBody()
          );
          throw new AuthenticationFailedException(message);
        });
    return response.getBody().getObject().getString("access_token");
  }

  @Override
  public void deleteUser(String jwt, UUID userId) {
    repository.deleteUser(jwt, userId)
        .ifFailure(res -> {
          String message = String.format(
              "Error deleting %s from Keycloak. Petition response: %d, {%s}",
              userId, res.getStatus(), res.getBody()
          );
          throw new InternalException(message);
        });
  }

  @Override
  public void createUser(String jwt, User user) {
    KeycloakUserRequest userRequest = mapper.toUserRequest(user);
    repository.createUser(jwt, userRequest)
        .ifFailure(res -> {
          String message = String.format(
              "Error creating %s on Keycloak. Petition response: %d, {%s}",
              user.getUsername(), res.getStatus(), res.getBody()
          );
          throw new InternalException(message);
        });    
  }

  @Override
  public User getUser(String jwt, String username) {
    KeycloakFiltersRequest filtersRequest = KeycloakFiltersRequest.singleUserFilter(username);
    HttpResponse<JsonNode> getUserResponse = repository.getUser(jwt, filtersRequest)
        .ifFailure(res -> {
          String message = String.format(
              "Error getting %s from Keycloak. Petition response: %d, {%s}",
              username, res.getStatus(), res.getBody()
          );
          throw new InternalException(message);
        });
    
    return mapper.toUser(getUserResponse.getBody().getArray().getJSONObject(0));
  }

  @Override
  public void addRole(String jwt, User user) {
    KeycloakRoleRequest roleRequest = mapper.toRoleRequest(user.getRole());
    repository.addRoleToUser(jwt, roleRequest, user.getId())
        .ifFailure(res -> {
          String message = String.format(
              "Error adding role %s to %s on Keycloak. Petition response: %d, {%s}",
              user.getRole().getName(), user.getId(), res.getStatus(), res.getBody()
          );
          throw new InternalException(message);
        });
  }

  @Override
  public void updatePassword(String jwt, User user) {
    KeycloakPasswordRequest passwordRequest = new KeycloakPasswordRequest(user.getPassword());
    repository.updatePassword(jwt, passwordRequest, user.getId())
        .ifFailure(res -> {
          String message = String.format(
              "Error updating password for user %s on Keycloak. Petition response: %d, {%s}",
              user.getId(), res.getStatus(), res.getBody()
          );
          throw new InternalException(message);
        });
  }

}
