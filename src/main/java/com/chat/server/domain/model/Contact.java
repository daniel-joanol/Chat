package com.chat.server.domain.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.chat.server.application.util.TimeUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Contact {
  
  private UUID id;
  private User user;
  private User friend;
  private ZonedDateTime createdAt;
  private Boolean isActive;

  public static Contact fromCreationRequest(UUID userId, String friendUsername) {
    return new Contact()
        .setUser(new User().setId(userId))
        .setFriend(new User().setUsername(friendUsername))
        .setCreatedAt(TimeUtil.now())
        .setIsActive(true);
  }

}
