package com.chat.server.infrastructure.repository.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "property")
public class PropertyEntity {
  
  @Id
  private Long id;

  private String name;

  private String value;
  
}
