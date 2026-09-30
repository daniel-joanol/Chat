# Messenger client

This folder contains a small Python client for the Chat WebSocket service.

It logs in with the REST login endpoint, opens the STOMP WebSocket, subscribes to direct messages and errors, lets you send chat messages, keeps listening for incoming messages, and supports logout.

## Requirements

- Python 3.10+
- `requests`
- `websocket-client`

Install dependencies:

```bash
pip install requests websocket-client
```

## Usage

1. Edit the credentials in `messenger.py`:

```python
USERNAME = "alice"
PASSWORD = "Password123!"
TARGET_USERNAME = "Dani"
```

2. Run the client:

```bash
python messenger.py
```

3. You will see a prompt:

```text
You> hi Dani
```

4. Type your message and press Enter.

5. Incoming replies appear in the terminal.

6. Type `exit` or `quit` to leave the session.

7. Type `logout` to log out from the server and then exit.

## How it works

- `POST /rest/v1/public/login` obtains the JWT access token.
- The client connects to `ws://localhost:20002/ws`.
- It sends a STOMP `CONNECT` frame with `Authorization: Bearer <token>`.
- It subscribes to:
  - `/user/queue/direct-messages`
  - `/user/queue/errors`
- It sends messages to:
  - `/app/direct-messages`

## Notes

- The target username is sent as `recipientUsername`, not as `@Dani`.
- The app does not put the token in the WebSocket URL.
- To log out, the server endpoint is:

```http
POST /rest/v1/internal/user/logout
Authorization: Bearer <token>
```
