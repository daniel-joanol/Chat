package com.chat.server.application.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.chat.server.domain.util.SecurityUtil;
import com.chat.server.infrastructure.exception.InternalException;

@Component
public class JWTSecurityUtil implements SecurityUtil {

  @Override
  public String getUsername() {
    Jwt jwt = this.getJWT();
    return jwt.hasClaim("preferred_username")
        ? jwt.getClaimAsString("preferred_username")
        : jwt.getClaimAsString("username");
  }

  private Jwt getJWT() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean noAuthFound = authentication == null 
        || !authentication.isAuthenticated()
        || !authentication.getPrincipal().getClass().isAssignableFrom(Jwt.class);
    if (noAuthFound) throw new InternalException("Could not get logged user");
    return (Jwt) authentication.getPrincipal();
  }

}
