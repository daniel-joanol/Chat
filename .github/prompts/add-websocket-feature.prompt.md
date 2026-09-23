---
description: "Add or change an authenticated STOMP-over-WebSocket feature using the ChatServer messaging contract."
agent: "ChatServer"
tools: [read, edit, search, execute, todo]
argument-hint: "WebSocket feature and behavior, e.g. 'add a conversation history subscription'"
---

Add or change an authenticated STOMP-over-WebSocket feature in ChatServer.

Read [AGENTS.md](../AGENTS.md), especially the WebSocket security and direct-message contract, before editing.

## Requirements

1. Keep WebSocket broker configuration in `application/config/`. Keep STOMP authentication, transport-specific error handling, and other framework adapters in `infrastructure/`.
2. Authenticate the STOMP `CONNECT` frame using a bearer token. Do not move JWT authentication to the browser WebSocket handshake or expose tokens in a URL.
3. Explicitly allow only the required `SEND` and `SUBSCRIBE` destinations. Do not allow raw broker destinations for clients.
4. Never receive the sender identity in the payload. Resolve it from `SecurityUtil.getUsername()` in the application service and apply authorization there.
5. Persist durable state before publishing live messages. Add or update domain model, ports, service, JPA adapter, MapStruct mapper, and Flyway migration when the feature has durable data.
6. Route typed domain errors through `GlobalWebSocketExceptionHandler` to the originating user's `/user/queue/errors` queue. Do not add message-specific exception handling to a controller.
7. Update [README.md](../../README.md) and [AGENTS.md](../AGENTS.md) whenever the public STOMP contract, authorization policy, destination, or payload changes.
8. Add focused pure unit tests for service authorization, STOMP controller publishing, interceptor access control, and error mapping as applicable. Run `mvn test` before finishing.