package com.chat.server.infrastructure.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.chat.server.domain.model.DirectMessage;
import com.chat.server.infrastructure.controller.response.DirectMessageResponse;

@Mapper(componentModel = "spring")
public interface DirectMessageDtoMapper {

  @Mapping(target = "senderUsername", source = "sender.username")
  @Mapping(target = "recipientUsername", source = "recipient.username")
  DirectMessageResponse toResponse(DirectMessage domain);

}