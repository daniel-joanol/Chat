package com.chat.server.infrastructure.repository.jpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.chat.server.domain.model.Contact;
import com.chat.server.domain.model.User;
import com.chat.server.infrastructure.repository.jpa.entity.ContactEntity;
import com.chat.server.infrastructure.repository.jpa.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

  final ContactEntityMapper contactMapper = Mappers.getMapper(ContactEntityMapper.class);
  
  @Mapping(target = "contacts", ignore = true)
  UserEntity toEntity(User domain);

  @Mapping(target = "contacts", source = "contacts", qualifiedByName = "contactsToDomain")
  User toDomain(UserEntity entity);

  @Named("contactsToDomain")
  default List<Contact> contactsToDomain(List<ContactEntity> contacts) {
    return contactMapper.toDomain(contacts);
  }

}
