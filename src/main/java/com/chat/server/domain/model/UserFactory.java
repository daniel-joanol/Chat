package com.chat.server.domain.model;

import java.util.ArrayList;

import com.chat.server.application.util.TimeUtil;
import com.chat.server.domain.enumerator.UserRoleEnum;
import com.chat.server.domain.enumerator.UserStatusEnum;
import com.chat.server.domain.enumerator.UserTypeEnum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFactory {
  
  public static User createInternalUser(String username, String email, char[] password) {
    Role role = new Role().setName(UserRoleEnum.ADMIN);
    return new User()
        .setUsername(username)
        .setEmail(email)
        .setPassword(password)
        .setCreatedAt(TimeUtil.now())
        .setStatus(UserStatusEnum.OFFLINE)
        .setType(UserTypeEnum.INTERNAL)
        .setRole(role)
        .setIsActive(true)
        .setContacts(new ArrayList<>());
  }

  public static User createExternalUser(String username, String email, char[] password) {
    Role role = new Role().setName(UserRoleEnum.USER);
    return new User()
        .setUsername(username)
        .setEmail(email)
        .setPassword(password)
        .setCreatedAt(TimeUtil.now())
        .setStatus(UserStatusEnum.OFFLINE)
        .setType(UserTypeEnum.EXTERNAL)
        .setRole(role)
        .setIsActive(true)
        .setContacts(new ArrayList<>());
  }

}
