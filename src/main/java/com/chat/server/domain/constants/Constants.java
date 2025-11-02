package com.chat.server.domain.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

  private static final String REST_PREFIX_URL = "/rest/v1";

  public static final String PUBLIC_CONTROLLER = REST_PREFIX_URL + "/public";
  public static final String USER_CONTROLLER = REST_PREFIX_URL + "/user";

  public static final String PROPERTY_DEFAULT_KEYCLOAK_USER = "DEFAULT_KEYCLOAK_USER";
  public static final String PROPERTY_DEFAULT_KEYCLOAk_USER_PASS = "DEFAULT_KEYCLOAk_USER_PASS";
  
}
