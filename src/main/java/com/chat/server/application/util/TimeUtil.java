package com.chat.server.application.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtil {
  
  public static ZonedDateTime now() {
    ZoneId zone = ZoneId.of("UTC");
    return ZonedDateTime.now(zone);
  }

}
