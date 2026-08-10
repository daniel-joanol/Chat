package com.chat.server.domain.service;

import java.util.List;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.exception.AuthenticationFailedException;
import com.chat.server.infrastructure.exception.ConflictException;

import com.chat.server.domain.model.User;

/**
 * Service port for user management operations.
 */
public interface UserService {

  /**
   * Retrieve a user by their username.
   *
   * @param username the username to search for
   * @return the matching User
   * @throws EntityNotFoundException when the user does not exist
   */
  User getByUsername(String username) throws EntityNotFoundException;
   
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
  String authenticate(String username, String password) throws AuthenticationFailedException, EntityNotFoundException;

  /**
   * Create a new external user and persist the domain user.
   *
   * This operation includes role assignment, user creation in the external identity provider,
   * and password initialization.
   *
   * @param user the user data to create
   * @return the created user with persisted fields populated
   * @throws ConflictException when the username or email is already taken
   * @throws com.chat.server.infrastructure.exception.InternalException when the external identity provider request fails
   */
  User createUser(User user) throws ConflictException;

  /**
   * Update a user's password in the Keycloak external identity store.
   *
   * @param user the user whose password should be updated
   * @throws com.chat.server.infrastructure.exception.InternalException when the external identity provider request fails
   */
  void updatePassword(User user);

  /**
   * Delete a user and their identity from the external identity provider.
   *
   * @param user the user to delete
   * @throws com.chat.server.infrastructure.exception.EntityNotFoundException when the user does not exist
   * @throws com.chat.server.infrastructure.exception.InternalException when the external identity provider request fails
   */
  void deleteUser(User user);

  /**
   * Delete a user by username.
   *
   * @param username the username of the user to delete
   * @throws com.chat.server.infrastructure.exception.EntityNotFoundException when the user does not exist
   * @throws com.chat.server.infrastructure.exception.InternalException when the external identity provider request fails
   */
  void deleteUser(String username);

  /**
   * Return users whose registration is incomplete and should be purged.
   *
   * @return list of incomplete users
   */
  List<User> getIncompleteUsers();
    
  /**
   * Mark the specified user as offline.
   *
   * This updates only the user's local status and does not revoke any existing access token.
   *
   * @param username the username to logout
   * @throws com.chat.server.infrastructure.exception.EntityNotFoundException when the user does not exist
   */
  void logout(String username);
   
}
