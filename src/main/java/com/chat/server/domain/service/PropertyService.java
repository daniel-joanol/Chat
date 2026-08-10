package com.chat.server.domain.service;

import com.chat.server.domain.model.User;

/**
 * Service port for application property retrieval.
 */
public interface PropertyService {
  
  /**
   * Retrieve the configured default internal user.
   *
   * @return the default internal User for system operations
   * @throws com.chat.server.infrastructure.exception.InternalException when the default user configuration cannot be resolved
   */
  User getDefaultInternalUser();

}
