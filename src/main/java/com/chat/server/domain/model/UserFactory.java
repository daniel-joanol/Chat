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
    return new User()
        .setUsername(username)
        .setEmail(email)
        .setPassword(password)
        .setCreatedAt(TimeUtil.now())
        .setStatus(UserStatusEnum.OFFLINE)
        .setType(UserTypeEnum.INTERNAL)
        .setRole(UserRoleEnum.USER)
        .setIsActive(true)
        .setContacts(new ArrayList<>());
  }

  public static User createExternalUser(String username, String email, char[] password) {
    return new User()
        .setUsername(username)
        .setEmail(email)
        .setPassword(password)
        .setCreatedAt(TimeUtil.now())
        .setStatus(UserStatusEnum.OFFLINE)
        .setType(UserTypeEnum.EXTERNAL)
        .setRole(UserRoleEnum.ADMIN)
        .setIsActive(true)
        .setContacts(new ArrayList<>());
  }

}
