package com.chat.server.domain.service;


import com.chat.server.infrastructure.exception.BadRequestException;
import com.chat.server.infrastructure.exception.ConflictException;
import com.chat.server.infrastructure.exception.EntityNotFoundException;
import com.chat.server.infrastructure.exception.ForbiddenException;
import java.util.UUID;

import com.chat.server.domain.model.Contact;

/**
 * Service port for managing user contact relationships.
 */
public interface ContactService {
  
  /**
   * Add a new contact relationship for the current user.
   *
   * @param contactUsername the username to add as a contact
   * @return the created Contact
   * @throws BadRequestException when the user attempts to add themselves
   * @throws ConflictException when the contact already exists
   * @throws EntityNotFoundException when either user is not found
   */
  Contact addContact(String contactUsername)
      throws BadRequestException,
             com.chat.server.infrastructure.exception.ConflictException,
             com.chat.server.infrastructure.exception.EntityNotFoundException;

  /**
   * Delete a contact relationship belonging to the current authenticated user.
   *
   * @param id the identifier of the contact to delete
   * @throws ForbiddenException when the contact does not belong to the current user
   */
  void delete(UUID id)
      throws ForbiddenException;

}
