package com.chat.server.domain.dao;


import com.chat.server.infrastructure.exception.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

import com.chat.server.domain.model.User;

/**
 * Port for user persistence operations.
 */
public interface UserDao {

  /**
   * Retrieve a user by its identifier.
   *
   * @param id the user id
   * @return the matching User
   * @throws EntityNotFoundException when the user does not exist
   */
  User getById(UUID id)
      throws EntityNotFoundException;

  /**
   * Save a user.
   *
   * @param user the user to save
   * @return the saved user instance
   */
  User save(User user);
  
  /**
   * Retrieve a user by username.
   *
   * @param username the username to search for
   * @return the matching User
   * @throws EntityNotFoundException when the user does not exist
   */
  User getByUsername(String username)
      throws EntityNotFoundException;

  /**
   * Check whether a user with the given username exists.
   *
   * @param username the username to check
   * @return true when the user exists
   */
  boolean existsByUsername(String username);

  /**
   * Check whether a user with the given email exists.
   *
   * @param email the email to check
   * @return true when the email is already assigned to a user
   */
  boolean existsByEmail(String email);

  /**
   * Delete a user by identifier.
   *
   * @param id the user id to delete
   */
  void delete(UUID id);

  /**
   * Return users whose registration is incomplete and should be purged.
   *
   * @return list of incomplete users
   */
  List<User> getIncompleteUsers();

}
