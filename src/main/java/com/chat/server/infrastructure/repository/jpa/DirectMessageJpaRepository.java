package com.chat.server.infrastructure.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.server.infrastructure.repository.jpa.entity.DirectMessageEntity;

public interface DirectMessageJpaRepository extends JpaRepository<DirectMessageEntity, UUID> {

}