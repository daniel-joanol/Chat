package com.chat.server.domain.service;

public interface AuthenticationService {

  String authenticate(String username, String password);

  String getInternalUserJwt(boolean forceAuthentication);
  
}
