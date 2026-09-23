package com.chat.server.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.chat.server.domain.constants.Constants;
import com.chat.server.infrastructure.websocket.JwtStompAuthenticationInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final JwtStompAuthenticationInterceptor authenticationInterceptor;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/queue");
    registry.setApplicationDestinationPrefixes(Constants.WEBSOCKET_APPLICATION_PREFIX);
    registry.setUserDestinationPrefix(Constants.WEBSOCKET_USER_PREFIX);
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(authenticationInterceptor, securityContextChannelInterceptor());
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint(Constants.WEBSOCKET_ENDPOINT);
  }

  @Bean
  public SecurityContextChannelInterceptor securityContextChannelInterceptor() {
    return new SecurityContextChannelInterceptor();
  }

}