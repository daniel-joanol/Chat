package com.chat.server.application.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.chat.server.domain.util.SecurityUtil;

@Component
public class JWTSecurityUtil implements SecurityUtil {

  @Override
  public String getUsername() {
    String username = null;
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal() instanceof Jwt jwt) {
      username = jwt.getClaimAsString("preferred_username");

      if (username == null) {
        username = jwt.getClaimAsString("username");
      }
    }

    return username;
  }

}
