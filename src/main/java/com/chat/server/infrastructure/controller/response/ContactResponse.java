package com.chat.server.infrastructure.controller.response;

import com.chat.server.domain.enumerator.UserStatusEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ContactResponse {

  private String username;
  private UserStatusEnum status;

}
