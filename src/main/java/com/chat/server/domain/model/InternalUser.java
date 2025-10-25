package com.chat.server.domain.model;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.chat.server.domain.enumerator.UserStatusEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class InternalUser {

  private UUID id;
  private String username;
  private String email;
  private ZonedDateTime createdAt;
  private UserStatusEnum status;
  private List<InternalUser> contacts;
  
}
