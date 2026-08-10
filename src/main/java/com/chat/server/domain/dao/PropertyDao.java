package com.chat.server.domain.dao;

import com.chat.server.infrastructure.exception.InternalException;/**
 * Port for reading application properties from the persistence layer.
 */
public interface PropertyDao {
  
  /**
   * Retrieve a required property value by its name.
   *
   * @param name the property name
   * @return the property value when the property exists
   * @throws InternalException when the property is missing or cannot be resolved
   */
  String getValueByName(String name)
      throws InternalException;

}
