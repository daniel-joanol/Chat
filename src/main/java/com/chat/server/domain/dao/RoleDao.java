package com.chat.server.domain.dao;

import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.domain.model.Role;

public interface RoleDao {
  
  Role getByName(UserRoleEnum name);

}
