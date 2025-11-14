package com.chat.server.infrastructure.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chat.server.domain.constants.Constants;
import com.chat.server.domain.service.ContactService;
import com.chat.server.infrastructure.controller.mapper.ContactDtoMapper;
import com.chat.server.infrastructure.controller.request.ContactRequest;
import com.chat.server.infrastructure.controller.response.ContactResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.CONTACT_CONTROLLER)
@Tag(
    name = "Contact Controller",
    description = """
        Controller to manage user contacts.
        Only role USER allowed.
        Gets the user from the JWT token.
        """
)
@PreAuthorize(Constants.HAS_ROLE_USER)
public class ContactController {
  
  private final ContactService service;
  private final ContactDtoMapper mapper;

  @Operation(
      summary = "Add contact",
      description = "Endpoint to add contact to user."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Created"),
      @ApiResponse(responseCode = "400", description = "Bad request"),
      @ApiResponse(responseCode = "404", description = "Username not found"),
      @ApiResponse(responseCode = "409", description = "Contact duplicated")
  })
  @PostMapping
  public ResponseEntity<ContactResponse> add(
      @RequestBody @Valid ContactRequest request
  ) {
    var contact = service.addContact(request.contactUsername());
    var response = mapper.toResponse(contact);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);
  }

  @Operation(
      summary = "Delete contact",
      description = "Endpoint to delete contact."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "204", description = "Contact removed"),
      @ApiResponse(responseCode = "403", description = "Contact does not belong to user")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @PathVariable UUID id
  ) {
    service.delete(id);
    return ResponseEntity
        .noContent()
        .build();
  }

}
