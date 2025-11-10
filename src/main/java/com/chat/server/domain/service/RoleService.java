package com.chat.server.domain.service;

import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.domain.model.Role;

public interface RoleService {
  
  Role getByName(UserRoleEnum name);
  
}
