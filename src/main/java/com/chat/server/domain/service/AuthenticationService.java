package com.chat.server.domain.service;

import com.chat.server.infrastructure.exception.AuthenticationFailedException;
import com.chat.server.infrastructure.exception.EntityNotFoundException;

/**
 * Service port for authentication operations
 */
public interface AuthenticationService {

  /**
   * Authenticate an existing user and return a JWT access token.
   * Successful authentication also updates the user's status to ONLINE.
   *
   * @param username the username to authenticate
   * @param password the password to validate
   * @return a JWT access token for the authenticated user
   * @throws AuthenticationFailedException when credentials are invalid
   * @throws EntityNotFoundException when the user does not exist
   */
  String authenticate(String username, String password)
      throws AuthenticationFailedException, EntityNotFoundException;

  /**
   * Get a JWT access token for the internal user.
   * If forceAuthentication is true, it will re-authenticate the internal user.
   *
   * @param forceAuthentication whether to force re-authentication even though the token may not have expired
   * @return a JWT access token for the internal user
   */
  String getInternalUserJwt(boolean forceAuthentication);
  
}
