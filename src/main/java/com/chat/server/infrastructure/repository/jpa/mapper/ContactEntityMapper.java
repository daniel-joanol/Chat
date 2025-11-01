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
public interface ContactEntityMapper {

  final UserEntityMapper userMapper = Mappers.getMapper(UserEntityMapper.class);

  @Mapping(target = "user", source = "user", qualifiedByName = "userToEntity")
  @Mapping(target = "friend", source = "friend", qualifiedByName = "userToEntity")
  ContactEntity toEntity(Contact domain);

  @Named("userToEntity")
  default UserEntity userToEntity(User user) {
    return userMapper.toEntity(user);
  }

  @Mapping(target = "user", source = "user", qualifiedByName = "userToDomain")
  @Mapping(target = "friend", source = "friend", qualifiedByName = "userToDomain")
  Contact toDomain(ContactEntity entity);
  List<Contact> toDomain(List<ContactEntity> entities);

  @Named("userToDomain")
  default User userToDomain(UserEntity user) {
    return userMapper.toDomainWithoutContacts(user);
  }
  
}
