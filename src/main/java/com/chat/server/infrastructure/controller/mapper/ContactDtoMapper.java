package com.chat.server.infrastructure.controller.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.chat.server.domain.model.Contact;
import com.chat.server.infrastructure.controller.request.ContactRequest;
import com.chat.server.infrastructure.controller.response.ContactResponse;

@Mapper(componentModel = "spring")
public interface ContactDtoMapper {
  
  @Mapping(target = "username", source = "friend.username")
  @Mapping(target = "status", source = "friend.status")
  ContactResponse toResponse(Contact domain);
  List<ContactResponse> toResponse(List<Contact> domains);

  default Contact toDomain(ContactRequest request) {
    return Contact.fromCreationRequest(request.userId(), request.contactUsername());
  }

}
