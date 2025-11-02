package com.chat.server.infrastructure.controller.response;

import java.util.List;
import java.util.UUID;

import com.chat.server.domain.enumerator.UserRoleEnum;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class UserResponse {
  
  private UUID id;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private UserRoleEnum roleName;
  private List<ContactResponse> contacts;

}
