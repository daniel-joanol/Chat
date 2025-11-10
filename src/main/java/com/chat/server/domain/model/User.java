package com.chat.server.domain.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.chat.server.domain.enumerator.UserStatusEnum;
import com.chat.server.domain.enumerator.UserTypeEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class User {

  private UUID id;
  private UUID keycloakId;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private ZonedDateTime createdAt;
  private UserStatusEnum status;
  private UserTypeEnum type;
  private Role role;
  private Boolean isEnabled;
  private Boolean isCreationCompleted;
  private List<Contact> contacts;
  
}
