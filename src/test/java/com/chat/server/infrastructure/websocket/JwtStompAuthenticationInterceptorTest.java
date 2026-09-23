package com.chat.server.infrastructure.websocket;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.converter.Converter;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@ExtendWith(MockitoExtension.class)
class JwtStompAuthenticationInterceptorTest {

  @Mock
  private JwtDecoder jwtDecoder;

  @Mock
  private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter;

  private JwtStompAuthenticationInterceptor sut;

  @BeforeEach
  void setUp() {
    sut = new JwtStompAuthenticationInterceptor(jwtDecoder, jwtAuthenticationConverter);
  }

  @Test
  void testPreSend_whenConnectHasNoBearerToken_thenThrowBadCredentialsException() {
    var message = message(StompCommand.CONNECT, null, null);

    assertThrows(
        org.springframework.security.authentication.BadCredentialsException.class,
        () -> sut.preSend(message, null)
    );
  }

  @Test
  void testPreSend_whenSendDestinationIsNotAllowed_thenThrowAccessDeniedException() {
    var authentication = new UsernamePasswordAuthenticationToken(
        "alice",
        null,
        List.of(new SimpleGrantedAuthority("ROLE_USER")));
    var message = message(StompCommand.SEND, "/app/not-allowed", authentication);

    assertThrows(AccessDeniedException.class, () -> sut.preSend(message, null));
  }

  @Test
  void testPreSend_whenSubscribeDestinationIsNotAllowed_thenThrowAccessDeniedException() {
    var authentication = new UsernamePasswordAuthenticationToken(
        "alice",
        null,
        List.of(new SimpleGrantedAuthority("ROLE_USER")));
    var message = message(StompCommand.SUBSCRIBE, "/queue/direct-messages", authentication);

    assertThrows(AccessDeniedException.class, () -> sut.preSend(message, null));
  }

  @Test
  void testPreSend_whenConnectHasValidBearerToken_thenAuthenticateConnection() {
    var jwt = Jwt.withTokenValue("token")
        .header("alg", "none")
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(60))
        .subject("alice")
        .build();
    AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        jwt,
        null,
        List.of(new SimpleGrantedAuthority("ROLE_USER")));
    when(jwtDecoder.decode("token")).thenReturn(jwt);
    when(jwtAuthenticationConverter.convert(jwt)).thenReturn(authentication);
    var message = connectMessage("Bearer token");

    var result = sut.preSend(message, null);

    org.junit.jupiter.api.Assertions.assertEquals(authentication, StompHeaderAccessor.wrap(result).getUser());
  }

  private Message<byte[]> message(StompCommand command, String destination, AbstractAuthenticationToken authentication) {
    StompHeaderAccessor accessor = StompHeaderAccessor.create(command);
    accessor.setDestination(destination);
    accessor.setUser(authentication);
    accessor.setLeaveMutable(true);
    return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
  }

  private Message<byte[]> connectMessage(String authorization) {
    StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
    accessor.setNativeHeader("Authorization", authorization);
    accessor.setLeaveMutable(true);
    return MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
  }

}