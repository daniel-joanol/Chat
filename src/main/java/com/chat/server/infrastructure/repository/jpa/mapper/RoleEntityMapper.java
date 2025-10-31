package com.chat.server.infrastructure.repository.jpa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.chat.server.domain.model.Role;
import com.chat.server.infrastructure.repository.jpa.entity.RoleEntity;

@Mapper(componentModel = "spring")
public interface RoleEntityMapper {
  
  @Mapping(target = "clientId", ignore = true)
  Role toDomain(RoleEntity entity);

  RoleEntity toEntity(Role domain);

}
