package com.chat.server.domain.service;

import java.util.List;

import com.chat.server.domain.model.User;
import com.chat.server.infrastructure.exception.AuthenticationFailedException;
import com.chat.server.infrastructure.exception.ConflictException;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.exception.InternalUserForbiddenException;

/**
* Service port for user management operations.
*/
public interface UserService {

 /**
  * Retrieve a user by username.
  *
  * @param username the username to search for
  * @return the matching user
  * @throws EntityNotFoundException when the user does not exist
  */
 User getByUsername(String username) throws EntityNotFoundException;

 /**
  * Create a new external user and persist the domain user.
  *
  * This operation includes role assignment, creation in the external identity provider,
  * and password initialization.
  *
  * @param user the user data to create
  * @return the created user with persisted fields populated
  * @throws ConflictException when the username or email is already taken
  * @throws InternalUserForbiddenException when the internal token is expired or invalid
  * @throws AuthenticationFailedException when the internal user cannot authenticate with the external provider
  */
 User createUser(User user)
     throws ConflictException, InternalUserForbiddenException, AuthenticationFailedException;

 /**
  * Update a user's password in the external identity store.
  *
  * @param user the user whose password should be updated
  * @throws EntityNotFoundException when the user does not exist
  * @throws InternalUserForbiddenException when the internal token is expired or invalid
  * @throws AuthenticationFailedException when the internal user cannot authenticate with the external provider
  */
 void updatePassword(User user)
     throws EntityNotFoundException, InternalUserForbiddenException, AuthenticationFailedException;

 /**
  * Delete a user and their identity from the external identity provider.
  *
  * @param user the user to delete
  * @throws EntityNotFoundException when the user does not exist
  * @throws InternalUserForbiddenException when the internal token is expired or invalid
  * @throws AuthenticationFailedException when the internal user cannot authenticate with the external provider
  */
 void deleteUser(User user)
     throws EntityNotFoundException, InternalUserForbiddenException, AuthenticationFailedException;

 /**
  * Delete a user by username.
  *
  * @param username the username of the user to delete
  * @throws EntityNotFoundException when the user does not exist
  * @throws InternalUserForbiddenException when the internal token is expired or invalid
  * @throws AuthenticationFailedException when the internal user cannot authenticate with the external provider
  */
 void deleteUser(String username)
     throws EntityNotFoundException, InternalUserForbiddenException, AuthenticationFailedException;

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
  * @throws EntityNotFoundException when the user does not exist
  */
 void logout(String username) throws EntityNotFoundException;

}
