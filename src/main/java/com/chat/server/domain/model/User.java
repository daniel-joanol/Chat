package com.chat.server.domain.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.chat.server.domain.enumerator.UserStatusEnum;
import com.chat.server.domain.enumerator.UserTypeEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class User {

  private UUID id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private char[] password;
  private ZonedDateTime createdAt;
  private UserStatusEnum status;
  private UserTypeEnum type;
  private Role role;
  private Boolean isActive;
  private List<User> contacts;
  
}
