package com.chat.server.domain.dao;

import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.domain.model.Role;

/**
 * Port for role persistence operations.
 */
public interface RoleDao {
  
  /**
   * Retrieve a role by its enumerated name.
   *
   * @param name the role name to search for
   * @return the matching Role
   * @throws com.chat.server.infrastructure.exception.InternalException when the role cannot be resolved
   */
  Role getByName(UserRoleEnum name);

}
