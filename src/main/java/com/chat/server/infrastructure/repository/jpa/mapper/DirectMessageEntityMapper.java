package com.chat.server.infrastructure.repository.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.chat.server.domain.model.DirectMessage;
import com.chat.server.domain.model.User;
import com.chat.server.infrastructure.repository.jpa.entity.DirectMessageEntity;
import com.chat.server.infrastructure.repository.jpa.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface DirectMessageEntityMapper {

  UserEntityMapper userMapper = Mappers.getMapper(UserEntityMapper.class);

  @Mapping(target = "sender", source = "sender", qualifiedByName = "userToEntity")
  @Mapping(target = "recipient", source = "recipient", qualifiedByName = "userToEntity")
  DirectMessageEntity toEntity(DirectMessage domain);

  @Named("userToEntity")
  default UserEntity userToEntity(User user) {
    return userMapper.toEntity(user);
  }

  @Mapping(target = "sender", source = "sender", qualifiedByName = "userToDomain")
  @Mapping(target = "recipient", source = "recipient", qualifiedByName = "userToDomain")
  DirectMessage toDomain(DirectMessageEntity entity);

  @Named("userToDomain")
  default User userToDomain(UserEntity user) {
    return userMapper.toDomainWithoutContacts(user);
  }

}