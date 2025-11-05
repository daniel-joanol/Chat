package com.chat.server.infrastructure.repository.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.chat.server.infrastructure.repository.jpa.entity.PropertyEntity;

public interface PropertyJpaRepository extends JpaRepository<PropertyEntity, Long> {

  @Query("""
      SELECT p.value
      FROM PropertyEntity p
      WHERE p.name = :name
      """)
  Optional<String> getValueByName(String name);

}
