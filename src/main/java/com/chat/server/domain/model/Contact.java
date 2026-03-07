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

  public static Contact fromCreationRequest(User user, User friend) {
    return new Contact()
        .setUser(user)
        .setFriend(friend)
        .setCreatedAt(TimeUtil.now());
    }

}
