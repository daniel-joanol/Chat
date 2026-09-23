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
public class DirectMessage {

  private UUID id;
  private User sender;
  private User recipient;
  private String content;
  private ZonedDateTime sentAt;

  public static DirectMessage fromCreationRequest(User sender, User recipient, String content) {
    return new DirectMessage()
        .setSender(sender)
        .setRecipient(recipient)
        .setContent(content)
        .setSentAt(TimeUtil.now());
  }

}