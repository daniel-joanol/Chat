CREATE TABLE direct_message (
  id UUID PRIMARY KEY,
  sender_id UUID NOT NULL REFERENCES _user(id),
  recipient_id UUID NOT NULL REFERENCES _user(id),
  content TEXT NOT NULL,
  sent_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT direct_message_sender_recipient_different CHECK (sender_id <> recipient_id)
);

CREATE INDEX direct_message_sender_sent_at_idx
    ON direct_message (sender_id, sent_at DESC);

CREATE INDEX direct_message_recipient_sent_at_idx
    ON direct_message (recipient_id, sent_at DESC);