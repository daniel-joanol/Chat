import json
import threading
import time
import requests
import websocket
import argparse
  

LOGIN_URL = "http://localhost:20002/rest/v1/public/login"
LOGOUT_URL = "http://localhost:20002/rest/v1/internal/user/logout"
WS_URL = "ws://localhost:20002/ws"

TOKEN = None
WS = None


def login(username: str, password: str) -> str:
  response = requests.post(
    LOGIN_URL,
    json={"username": username, "password": password},
    timeout=10,
    )
    
  if response.status_code != 200:
    raise RuntimeError(f"Login failed: {response.status_code} {response.text}")
  token = response.text.strip()
  print("Login OK")
  return token


def logout(token: str) -> None:
  headers = {"Authorization": f"Bearer {token}"}
  response = requests.post(LOGOUT_URL, headers=headers, timeout=10)
  if response.status_code not in (200, 204):
    print(f"Logout failed: {response.status_code} {response.text}")
    return
  print("Logout OK")


def stomp_frame(command: str, headers=None, body: str = "") -> str:
  frame = f"{command}\n"
  if headers:
    for key, value in headers.items():
      frame += f"{key}:{value}\n"
  frame += "\n"
  if body:
    frame += body
  frame += "\x00"
  return frame


def recv_stomp_frame(ws) -> dict | None:
  chunks = []
  while True:
    try:
      chunk = ws.recv()
    except Exception as exc:
      print(f"WebSocket receive error: {exc}")
      return None

    if isinstance(chunk, bytes):
      data = chunk
    else:
      data = chunk.encode("utf-8", "replace")

    if not data:
      continue

    chunks.append(data)
    if b"\x00" in data:
      full = b"".join(chunks)
      frame = full.split(b"\x00", 1)[0].decode("utf-8", "replace")
      break

    parts = frame.split("\n\n", 1)
    if len(parts) != 2:
      return {"command": "UNKNOWN", "body": frame}

    header_block, body = parts
    lines = header_block.split("\n")
    command = lines[0]
    headers = {}
    for line in lines[1:]:
      if ":" in line:
        key, value = line.split(":", 1)
        headers[key] = value

    return {
      "command": command,
      "headers": headers,
      "body": body,
    }


def listen_loop(ws):
  while True:
    frame = recv_stomp_frame(ws)
    if frame is None:
      break

    command = frame.get("command")
    body = frame.get("body", "")

    if command == "MESSAGE":
      print("\n--- INCOMING MESSAGE ---")
      try:
        payload = json.loads(body)
        print(json.dumps(payload, indent=2))
      except Exception:
        print(body)
        print("------------------------\n")
    elif command == "ERROR":
      print("\n--- STOMP ERROR ---")
      print(body)
      print("--------------------\n")
    elif command == "CONNECTED":
      print("STOMP connected")


def send_message(ws, recipient_username: str, content: str):
  payload = {
    "recipientUsername": recipient_username,
    "content": content,
  }
  ws.send(stomp_frame(
    "SEND",
    {
      "destination": "/app/direct-messages",
      "content-type": "application/json",
    },
    json.dumps(payload),
    ))
  print(f"\nSent to {recipient_username}: {content}\n")


def connect(token: str):
  ws = websocket.WebSocket()
  ws.connect(WS_URL)
  ws.send(stomp_frame(
    "CONNECT",
    {
      "Authorization": f"Bearer {token}",
      "accept-version": "1.2",
      "heart-beat": "10000,10000",
    },
  ))

  time.sleep(0.5)

  ws.send(stomp_frame(
    "SUBSCRIBE",
    {
      "id": "direct-messages",
      "destination": "/user/queue/direct-messages",
    },
  ))

  ws.send(stomp_frame(
    "SUBSCRIBE",
    {
      "id": "errors",
      "destination": "/user/queue/errors",
      },
  ))

  return ws


def main(username: str, password: str):
  global TOKEN, WS

  TOKEN = login(username, password)
  WS = connect(TOKEN)

  print(
    """
    Connected and subscribed\n
    To send a message, type -m 'message' -t recipient\n
    Example: -m 'Hello!' -t Dani\n
    Type 'logout' to log out\n
    """)

  listener = threading.Thread(target=listen_loop, args=(WS,), daemon=True)
  listener.start()

  while True:
    try:
      text = input("You> ").strip()
    except KeyboardInterrupt:
      print("\nClosing connection...")
      logout(TOKEN)
      break

    if not text:
      continue
      
    if text.startswith("-m ") and "-t " in text:
      try:
        message_part = text.split("-m ", 1)[1].split(" -t ", 1)
        text = message_part[0].strip("'\"")
        target = message_part[1].strip("'\"")
        send_message(WS, target, text)
      except Exception:
        print("Invalid format. Use: -m 'message' -t recipient")
        continue

    if WS is not None:
      try:
        WS.close()
      except Exception:
        pass
      
  print("Disconnected.")
   
   
if __name__ == "__main__":
  parser = argparse.ArgumentParser(description="Chat Messenger")
  parser.add_argument("-u", "--username", required=True, help="Username")
  parser.add_argument("-p", "--password", required=True, help="Password")
  args = parser.parse_args()
  main(args.username, args.password)
