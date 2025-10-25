package com.chat.server.domain.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Contact {
  
  private UUID id;
  private InternalUser user;
  private InternalUser friend;
  private ZonedDateTime createdAt;

}
