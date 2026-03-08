package com.chat.server.infrastructure.controller.apis.maintenance;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.server.domain.constants.Constants;
import com.chat.server.infrastructure.scheduled.PurgeIncompleteUsersTask;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.SCHEDULER_CONTROLLER)
@PreAuthorize(Constants.HAS_ROLE_ADMIN)
@Tag(
    name = "Scheduler Controller",
    description = """
        Controller to manage scheduled tasks.
        Only role ADMIN allowed.
        """
)
public class SchedulerController {

  private final PurgeIncompleteUsersTask purgeIncompleteUsersTask;

  @Operation(
      summary = "Purge incomplete users",
      description = "Endpoint to trigger purge of incomplete users."
  )
  @ApiResponse(responseCode = "200", description = "Purge started")
  @PostMapping("/purge-incomplete-users")
  public ResponseEntity<String> purgeIncompleteUsers() {
    purgeIncompleteUsersTask.asyncStart();
    return ResponseEntity.ok("Purge started");
  }
  
}
