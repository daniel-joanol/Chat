package com.chat.server.infrastructure.scheduled;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.chat.server.domain.model.User;
import com.chat.server.domain.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PurgeIncompleteUsersTask {

  private static final String LOG_START = "Starting purge of incomplete users{}";
  private static final String LOG_COMPLETE = "Purge of incomplete users completed. {} users purged.";

  private final UserService userService;

  @Scheduled(cron = "0 0 0 1 * ?")
  public void start() {
    log.info(LOG_START, "...");
    int purgedCount = purge();
    log.info(LOG_COMPLETE, purgedCount);
  }

  @Async
  public void asyncStart(String username) {
    log.info(LOG_START, " by user: " + username);
    int purgedCount = purge();
    log.info(LOG_COMPLETE, purgedCount);
  }

  public int purge() {
    int purgedCount = 0;
    List<User> usersToPurge = userService.getIncompleteUsers();
    for (User user : usersToPurge) {
      try {
        userService.deleteUser(user);
        purgedCount++;
      } catch (Exception e) {
        log.error("Failed to purge incomplete user: " + user.getUsername(), e.getMessage(), e);
      }
    }

    return purgedCount;
  }
  
}
