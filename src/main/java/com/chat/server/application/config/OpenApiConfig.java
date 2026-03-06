package com.chat.server.application.config;

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
    Schema<?> errorSchema = new ObjectSchema()
        .addProperty("timestamp", new StringSchema())
        .addProperty("traceId", new StringSchema())
        .addProperty("message", new StringSchema());

    ApiResponse genericError = new ApiResponse()
        .description("Standard error response")
        .content(new Content().addMediaType("application/json",
            new MediaType()
                .schema(errorSchema)
                .example(
                    java.util.Map.of(
                        "timestamp", "2025-01-01T12:00:00Z",
                        "traceId", "123e4567-e89b-12d3-a456-426614174000",
                        "message", "Something went wrong"))));

    return new OpenAPI()
        .components(new Components()
            .addSecuritySchemes("bearerAuth",
                new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"))
            .addSchemas("ErrorResponse", errorSchema)
            .addResponses("GenericError", genericError))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
  }

}
