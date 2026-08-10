package com.chat.server.domain.dao;


import com.chat.server.infrastructure.exception.AuthenticationFailedException;
import com.chat.server.infrastructure.exception.InternalException;
import java.util.UUID;

import com.chat.server.domain.model.User;

/**
 * Port for access management operations against the external identity provider.
 */
public interface AccessManagementDao {
  
  /**
   * Authenticate a user with the external identity provider.
   *
   * @param username the username to authenticate
   * @param password the password to validate
   * @return a JWT access token if authentication succeeds
   * @throws AuthenticationFailedException when credentials are invalid
   */
  String authenticate(String username, String password)
      throws AuthenticationFailedException;

  /**
   * Create a new user in the external identity provider.
   *
   * @param jwt the administrative JWT used to authorize the request
   * @param user the user to create
   * @throws InternalException when the external provider request fails
   */
  void createUser(String jwt, User user)
      throws InternalException;

  /**
   * Retrieve a user from the external identity provider.
   *
   * @param jwt the administrative JWT used to authorize the request
   * @param username the username of the user to retrieve
   * @return the retrieved User
   * @throws InternalException when the external provider request fails
   */
  User getUser(String jwt, String username)
      throws InternalException;

  /**
   * Update the password of an existing external user.
   *
   * @param jwt the administrative JWT used to authorize the request
   * @param user the user whose password should be updated
   * @throws InternalException when the external provider request fails
   */
  void updatePassword(String jwt, User user)
      throws InternalException;

  /**
   * Assign a role to an existing external user.
   * It gets the role from the user object.
   *
   * @param jwt the administrative JWT used to authorize the request
   * @param user the user to update with the role
   * @throws InternalException when the external provider request fails
   */
  void addRole(String jwt, User user)
      throws InternalException;

  /**
   * Delete a user from the external identity provider.
   *
   * @param jwt the administrative JWT used to authorize the request
   * @param userId the identifier of the user to delete
   * @throws InternalException when the external provider request fails
   */
  void deleteUser(String jwt, UUID userId)
      throws InternalException;

}
