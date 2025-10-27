package com.chat.server.infrastructure.repository.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

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

}