package com.chat.server.infrastructure.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.chat.server.infrastructure.repository.jpa.entity.ContactEntity;

public interface ContactJpaRepository extends JpaRepository<ContactEntity, UUID> {
  
  @Query("""
      SELECT COUNT(c.id) > 0
      FROM ContactEntity c
      WHERE c.user.username = :username AND c.friend.username = :contactUsername
      """)
  boolean exists(String username, String contactUsername);
}
