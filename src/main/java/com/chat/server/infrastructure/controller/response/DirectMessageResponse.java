package com.chat.server.infrastructure.controller.response;

import java.time.ZonedDateTime;
import java.util.UUID;

public record DirectMessageResponse(
    UUID id,
    String senderUsername,
    String recipientUsername,
    String content,
    ZonedDateTime sentAt
) {}