package com.chat.server.infrastructure.repository.http;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.chat.server.infrastructure.dao.http.request.KeycloakFiltersRequest;
import com.chat.server.infrastructure.dao.http.request.KeycloakPasswordRequest;
import com.chat.server.infrastructure.dao.http.request.KeycloakRoleRequest;
import com.chat.server.infrastructure.dao.http.request.KeycloakUserRequest;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;

@Component
public class KeycloakHttpRepository {

  @Value("${keycloak.url}")
  private String url;

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
  private String clientName;

  @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
  private String clientSecret;
  
  public HttpResponse<JsonNode> login(String username, String password) {
    String endpoint = String.format("%s/realms/%s/protocol/openid-connect/token", url, realm);
    return Unirest.post(endpoint)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .field("username", username)
        .field("password", password)
        .field("client_id", clientName)
        .field("scope", "openid")
        .field("client_secret", clientSecret)
        .field("grant_type", "password")
        .asJson();
  }

  public HttpResponse<JsonNode> createUser(String jwt, KeycloakUserRequest request) {
    String endpoint = String.format("%s/admin/realms/%s/users", url, realm);
    return Unirest.post(endpoint)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
        .body(request)
        .asJson();
  }

  public HttpResponse<JsonNode> getUser(String jwt, KeycloakFiltersRequest filter) {
    String endpoint = String.format("%s/admin/realms/%s/users", url, realm);
    Map<String, Object> queryParameters = Map.of(
        "briefRepresentation", true,
        "first", filter.getFirst(),
        "max", filter.getMax(),
        "exact", true,
        "search", filter.getSearch()
    );
    return Unirest.get(endpoint)
        .routeParam(queryParameters)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
        .asJson();
  }

  public HttpResponse<JsonNode> updatePassword(String jwt, KeycloakPasswordRequest request, UUID userId) {
    String endpoint = String.format("%s/admin/realms/%s/users/%s/reset-password", url, realm, userId);
    return Unirest.put(endpoint)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
        .body(request)
        .asJson();
  }

  public HttpResponse<JsonNode> addRoleToUser(String jwt, KeycloakRoleRequest request, UUID userId) {
    String endpoint = String.format("%s/admin/realms/%s/users/%s/role-mappings/clients/%s", url, realm, userId, request.getContainerId());
    return Unirest.post(endpoint)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
        .body(request)
        .asJson();
  }

  public HttpResponse<JsonNode> deleteUser(String jwt, UUID userId) {
    String endpoint = String.format("%s/admin/realms/%s/users/%s", url, realm, userId);
    return Unirest.delete(endpoint)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
        .asJson();
  }

}