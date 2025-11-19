package com.chat.server.application.config;

import java.util.List;
import java.util.Map;

import org.springdoc.core.customizers.OpenApiCustomizer;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

  @Bean
  public GroupedOpenApi v1Api() {
    return GroupedOpenApi.builder()
        .group("v1")
        .pathsToMatch("/**")
        .build();
  }

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components()
            // --- Security ---
            .addSecuritySchemes(
                "bearerAuth",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"))

            // --- Global error schema (timestamp, traceId, message) ---
            .addSchemas("ErrorResponse",
                new ObjectSchema()
                    .addProperty("timestamp", new StringSchema().example("2025-01-01T12:00:00Z"))
                    .addProperty("traceId", new StringSchema().example("123e4567-e89b-12d3-a456-426614174000"))
                    .addProperty("message", new StringSchema().example("Something went wrong")))

            // --- Global error response (reusable) ---
            .addResponses("GenericError",
                new ApiResponse()
                    .description("Standard error response")
                    .content(new Content().addMediaType(
                        "application/json",
                        new MediaType().schema(
                            new Schema<>().$ref("#/components/schemas/ErrorResponse"))))))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
  }

}
