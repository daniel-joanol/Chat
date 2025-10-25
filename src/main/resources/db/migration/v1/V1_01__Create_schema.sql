-- ADD EXTENSION SO POSTGRES CAN GENERATE UUIDS
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- CREATE INTERNAL_USERS
CREATE TABLE IF NOT EXISTS internal_user(
  id UUID DEFAULT uuid_generate_v4(),
  username text NOT NULL UNIQUE,
  email text NOT NULL UNIQUE,
  created_at TIMESTAMPTZ DEFAULT now(),
  status text,
  CONSTRAINT internal_user_pkey PRIMARY KEY (id)
);

-- CREATE CONTACT_LIST (RELATIONSHIP BETWEEN USERS)
CREATE TABLE IF NOT EXISTS contact(
  id UUID DEFAULT uuid_generate_v4(),
  user_id UUID NOT NULL,
  friend_id UUID NOT NULL,
  created_at TIMESTAMPTZ DEFAULT now(),
  CONSTRAINT contact_pkey PRIMARY KEY (id),
  CONSTRAINT contact_user_id_fkey FOREIGN KEY (user_id) REFERENCES internal_user(id),
  CONSTRAINT contact_friend_id_fkey FOREIGN KEY (friend_id) REFERENCES internal_user(id),
  CONSTRAINT contact_ukey UNIQUE (user_id, friend_id)
);