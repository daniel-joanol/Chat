package com.chat.server.infrastructure.websocket;

import java.util.Collection;
import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import com.chat.server.domain.constants.Constants;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtStompAuthenticationInterceptor implements ChannelInterceptor {

  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtDecoder jwtDecoder;
  private final Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    if (accessor == null || accessor.getCommand() == null) {
      return message;
    }

    StompCommand command = accessor.getCommand();
    if (StompCommand.CONNECT.equals(command)) {
      accessor.setUser(authenticate(accessor));
      return message;
    }

    Authentication authentication = getAuthentication(accessor);
    if (StompCommand.SEND.equals(command)) {
      verifyUserRole(authentication);
      verifyDestination(accessor.getDestination(), Constants.DIRECT_MESSAGE_DESTINATION);
    }

    if (StompCommand.SUBSCRIBE.equals(command)) {
      verifyUserRole(authentication);
      verifySubscriptionDestination(accessor.getDestination());
    }

    return message;
  }

  private Authentication authenticate(StompHeaderAccessor accessor) {
    String authorization = accessor.getFirstNativeHeader("Authorization");
    if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
      throw new BadCredentialsException("A bearer token is required for STOMP CONNECT");
    }

    try {
      Jwt jwt = jwtDecoder.decode(authorization.substring(BEARER_PREFIX.length()));
      return jwtAuthenticationConverter.convert(jwt);
    } catch (RuntimeException e) {
      throw new BadCredentialsException("Invalid bearer token", e);
    }
  }

  private Authentication getAuthentication(StompHeaderAccessor accessor) {
    if (accessor.getUser() instanceof Authentication authentication && authentication.isAuthenticated()) {
      return authentication;
    }

    throw new AccessDeniedException("Authenticated STOMP connection required");
  }

  private void verifyUserRole(Authentication authentication) {
    boolean userRolePresent = Optional.ofNullable(authentication.getAuthorities())
        .stream()
        .flatMap(Collection::stream)
        .map(GrantedAuthority::getAuthority)
        .anyMatch("ROLE_USER"::equals);
    if (!userRolePresent) {
      throw new AccessDeniedException("USER role required");
    }
  }

  private void verifyDestination(String destination, String expectedDestination) {
    if (!expectedDestination.equals(destination)) {
      throw new AccessDeniedException("STOMP destination is not allowed");
    }
  }

  private void verifySubscriptionDestination(String destination) {
    boolean allowedDestination = (Constants.WEBSOCKET_USER_PREFIX + Constants.DIRECT_MESSAGE_USER_DESTINATION)
        .equals(destination)
        || (Constants.WEBSOCKET_USER_PREFIX + Constants.WEBSOCKET_ERROR_DESTINATION).equals(destination);
    if (!allowedDestination) {
      throw new AccessDeniedException("STOMP destination is not allowed");
    }
  }

}