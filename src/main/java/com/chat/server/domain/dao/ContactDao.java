package com.chat.server.domain.dao;

import java.util.UUID;

import com.chat.server.domain.model.Contact;

/**
 * Port for contact persistence operations.
 */
public interface ContactDao {
  
  /**
   * Save a contact relationship.
   *
   * @param contact the contact to persist
   * @return the saved contact
   */
  Contact save(Contact contact);

  /**
   * Check whether a contact relation exists between two users.
   *
   * @param userUsername the owner username
   * @param contactUsername the contact username
   * @return true when the contact relation exists
   */
  boolean exists(String userUsername, String contactUsername);

  /**
   * Retrieve a contact relationship by its identifier.
   *
   * @param id the contact id
   * @return the matching Contact
   * @throws com.chat.server.infrastructure.exception.EntityNotFoundException when the contact does not exist
   */
  Contact getById(UUID id);

  /**
   * Delete a contact relationship by identifier.
   *
   * @param id the contact id to delete
   */
  void delete(UUID id);

}
