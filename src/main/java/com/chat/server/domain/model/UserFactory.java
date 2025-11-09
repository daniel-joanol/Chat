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
  
  public static User generateDefaultInternalUser(String username, String password) {
    return new User()
        .setUsername(username)
        .setPassword(password)
        .setContacts(new ArrayList<>());
  }

  public static User generateInternalUser(User user) {
    Role role = new Role().setName(UserRoleEnum.ADMIN);
    return setCommonValues(user)
        .setType(UserTypeEnum.INTERNAL)
        .setRole(role);
  }

  public static User generateExternalUser(User user) {
    Role role = new Role().setName(UserRoleEnum.USER);
    return setCommonValues(user)
        .setType(UserTypeEnum.EXTERNAL)
        .setRole(role);
  }

  private static User setCommonValues(User user) {
    return new User()
        .setUsername(user.getUsername())
        .setFirstName(user.getFirstName())
        .setLastName(user.getLastName())
        .setEmail(user.getEmail())
        .setPassword(user.getPassword())
        .setCreatedAt(TimeUtil.now())
        .setStatus(UserStatusEnum.OFFLINE)
        .setIsEnabled(true)
        .setIsCreationCompleted(false)
        .setContacts(new ArrayList<>());
  }

}
