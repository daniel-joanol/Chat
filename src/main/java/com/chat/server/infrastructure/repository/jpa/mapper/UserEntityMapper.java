package com.chat.server.infrastructure.repository.jpa.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.chat.server.domain.model.Contact;
import com.chat.server.domain.model.Role;
import com.chat.server.domain.model.User;
import com.chat.server.infrastructure.repository.jpa.entity.ContactEntity;
import com.chat.server.infrastructure.repository.jpa.entity.RoleEntity;
import com.chat.server.infrastructure.repository.jpa.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

  final RoleEntityMapper roleMapper = Mappers.getMapper(RoleEntityMapper.class);
  final ContactEntityMapper contactMapper = Mappers.getMapper(ContactEntityMapper.class);
  
  @Mapping(target = "contacts", ignore = true)
  @Mapping(target = "role", source = "role", qualifiedByName = "roleToEntity")
  UserEntity toEntity(User domain);

  @Named("roleToEntity")
  default RoleEntity roleToEntity(Role role) {
    return roleMapper.toEntity(role);
  }

  @Mapping(target = "contacts", source = "contacts", qualifiedByName = "contactsToDomain")
  @Mapping(target = "role", source = "role", qualifiedByName = "roleToDomain")
  User toDomain(UserEntity entity);

  @Named("contactsToDomain")
  default List<Contact> contactsToDomain(List<ContactEntity> contacts) {
    return contactMapper.toDomain(contacts);
  }

  @Named("roleToDomain")
  default Role roleToDomain(RoleEntity role) {
    return roleMapper.toDomain(role);
  }

}
