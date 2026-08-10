package com.chat.server.domain.service;


import com.chat.server.infrastructure.exception.InternalException;
import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.domain.model.Role;

/**
 * Service port for role resolution.
 */
public interface RoleService {
  
  /**
   * Retrieve a role by its enumerated name.
   *
   * @param name the role name to resolve
   * @return the corresponding Role
   * @throws InternalException when the role cannot be resolved
   */
  Role getByName(UserRoleEnum name)
      throws InternalException;
  
}
