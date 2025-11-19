package com.chat.server.application.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.chat.server.domain.constants.Constants;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/rest/v1/public/**",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html")
            .permitAll()
            .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(this.jwtAuthenticationConverter())));

    return http.build();
  }

  @Bean
  @SuppressWarnings("unchecked")
  public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
    authoritiesConverter.setAuthorityPrefix("ROLE_");
    authoritiesConverter.setAuthoritiesClaimName("roles");

    return jwt -> {
      Collection<GrantedAuthority> authorities = new HashSet<>(authoritiesConverter.convert(jwt));
      Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
      if (resourceAccess != null) {
        Map<String, Object> client = (Map<String, Object>) resourceAccess.get(Constants.KEYCLOAK_CLIENT);
        if (client != null) {
          List<String> clientRoles = (List<String>) client.get("roles");
          if (clientRoles != null) {
            clientRoles.forEach(r -> authorities.add(new SimpleGrantedAuthority("ROLE_" + r)));
          }
        }
      }

      return new JwtAuthenticationToken(jwt, authorities);
    };
  }

}
