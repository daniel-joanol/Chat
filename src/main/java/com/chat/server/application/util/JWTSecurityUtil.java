package com.chat.server.application.util;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.chat.server.domain.util.SecurityUtil;

@Component
public class JWTSecurityUtil implements SecurityUtil {

  @Override
  public String getUsername() {
    var user = (UserDetails) SecurityContextHolder
        .getContext().getAuthentication().getPrincipal();
    return user.getUsername();
  }

}
