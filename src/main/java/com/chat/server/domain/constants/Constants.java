package com.chat.server.domain.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

  private static final String REST_PREFIX_URL = "/rest/v1";
  public static final String INTERNAL_PREFIX_URL = REST_PREFIX_URL + "/internal";
  public static final String MAINTENANCE_PREFIX_URL = REST_PREFIX_URL + "/maintenance";

  public static final String PUBLIC_CONTROLLER = REST_PREFIX_URL + "/public";
  public static final String CONTACT_CONTROLLER = INTERNAL_PREFIX_URL + "/contact";
  public static final String SCHEDULER_CONTROLLER = MAINTENANCE_PREFIX_URL + "/scheduler";

  public static final String PROPERTY_DEFAULT_KEYCLOAK_USER = "DEFAULT_KEYCLOAK_USER";
  public static final String PROPERTY_DEFAULT_KEYCLOAK_USER_PASS = "DEFAULT_KEYCLOAk_USER_PASS";

  public static final String KEYCLOAK_CLIENT = "chat";

  public static final String ROLE_ADMIN = "ADMIN";
  public static final String ROLE_USER = "USER";

  public static final String HAS_ROLE_ADMIN = "hasRole('" + ROLE_ADMIN + "')";
  public static final String HAS_ROLE_USER = "hasRole('" + ROLE_USER + "')";
  
}
