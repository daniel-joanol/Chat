package com.chat.server.infrastructure.repository.jpa.entity;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "direct_message")
public class DirectMessageEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @UuidGenerator
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "sender_id")
  private UserEntity sender;

  @ManyToOne
  @JoinColumn(name = "recipient_id")
  private UserEntity recipient;

  private String content;
  private ZonedDateTime sentAt;

}