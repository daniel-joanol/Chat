package com.chat.server.infrastructure.repository.http;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.chat.server.infrastructure.repository.http.request.KeycloakPasswordRequest;
import com.chat.server.infrastructure.repository.http.request.KeycloakRoleRequest;
import com.chat.server.infrastructure.repository.http.request.KeycloakUserRequest;

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
  private String client;

  @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
  private String clientSecret;
  
  public HttpResponse<JsonNode> login(String username, char[] password) {
    String endpoint = String.format("%s/realms/%s/protocol/openid-connect/token", url, realm);
    return Unirest.post(endpoint)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        .field("username", username)
        .field("password", new String(password))
        .field("client_id", client)
        .field("scope", "openid")
        .field("client_secret", clientSecret)
        .asJson();
  }

  public HttpResponse<JsonNode> createUser(String jwt, KeycloakUserRequest request) {
    String endpoint = String.format("%s/admin/realms/%s/users", url, realm);
    return Unirest.post(endpoint)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
        .body(request)
        .asJson();
  }

  public HttpResponse<JsonNode> updatePassword(String jwt, KeycloakPasswordRequest request, UUID userId) {
    String endpoint = String.format("%s/admin/realms/%s/users/%s/reset-password", url, realm, userId);
    return Unirest.put(endpoint)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
        .body(request)
        .asJson();
  }

  public HttpResponse<JsonNode> addRoleToUser(String jwt, KeycloakRoleRequest request, UUID userId) {
    String endpoint = String.format("%s/admin/realms/%s/users/%s/role-mappings/clients/%s", url, realm, userId, request.getClientId());
    return Unirest.post(endpoint)
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