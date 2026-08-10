package com.chat.server.domain.service;


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
   */
  Role getByName(UserRoleEnum name);
  
}
