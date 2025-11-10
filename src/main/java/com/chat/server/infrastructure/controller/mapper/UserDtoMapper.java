package com.chat.server.infrastructure.controller.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.chat.server.domain.model.Contact;
import com.chat.server.domain.model.User;
import com.chat.server.infrastructure.controller.request.UserRequest;
import com.chat.server.infrastructure.controller.response.ContactResponse;
import com.chat.server.infrastructure.controller.response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
  
  final ContactDtoMapper contactMapper = Mappers.getMapper(ContactDtoMapper.class);

  @Mapping(target = "contacts", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "isCreationCompleted", ignore = true)
  @Mapping(target = "isEnabled", ignore = true)
  @Mapping(target = "keycloakId", ignore = true)
  @Mapping(target = "role", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "type", ignore = true)
  User toDomain(UserRequest request);

  @Mapping(target = "contacts", source = "contacts", qualifiedByName = "contactsToResponse")
  @Mapping(target = "roleName", source = "role.name")
  UserResponse toResponse(User domain);

  @Named("contactsToResponse")
  default List<ContactResponse> contactsToResponse(List<Contact> contacts) {
    return contacts == null
        ? new ArrayList<>()
        : contactMapper.toResponse(contacts);
  }

}
