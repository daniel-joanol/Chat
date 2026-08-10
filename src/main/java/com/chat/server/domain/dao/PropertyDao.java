package com.chat.server.domain.dao;

/**
 * Port for reading application properties from the persistence layer.
 */
public interface PropertyDao {
  
  /**
   * Retrieve a required property value by its name.
   *
   * @param name the property name
   * @return the property value when the property exists
   * @throws com.chat.server.infrastructure.exception.InternalException when the property is missing or cannot be resolved
   */
  String getValueByName(String name);

}
