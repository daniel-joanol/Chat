-- REMOVE COLUMN PASSWORD FROM _USER
DO $$
BEGIN

  IF EXISTS (
      SELECT 1
      FROM information_schema.columns
      WHERE table_schema = 'chat'
        AND table_name = '_user'
        AND column_name = 'password'
  ) THEN
    ALTER TABLE _user DROP COLUMN password;
  END IF;

END $$;

-- CREATE ROLE TABLE
CREATE TABLE IF NOT EXISTS role(
  id BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL UNIQUE,
  keycloak_id UUID UNIQUE
);

-- INSERT ROLES
INSERT INTO role(name) VALUES ('ADMIN'), ('USER') ON CONFLICT DO NOTHING;

-- CREATE PROPERTY TABLE
CREATE TABLE IF NOT EXISTS property(
  id BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL UNIQUE,
  value TEXT NOT NULL
);

-- INSERT PROPERTIES
INSERT INTO property(name, value) VALUES 
  ('DEFAULT_KEYCLOAK_USER', 'default_internal_user'),
  ('DEFAULT_KEYCLOAK_USER_ID', 'UPDATE USER ID'),
  ('DEFAULT_KEYCLOAk_USER_PASS', 'UPDATE PASSWORD ON IMPLANTATION'),
  ('DEFAULT_KEYCLOAK_CLIENT_ID', 'UPDATE CLIENT ID')
  ON CONFLICT DO NOTHING;

-- ADD COLUMNS TO USER
DO $$
BEGIN

  -- ADD ROLE
  IF EXISTS (
      SELECT 1
      FROM information_schema.columns
      WHERE table_schema = 'chat'
        AND table_name = '_user'
        AND column_name = 'role'
  ) THEN
    ALTER TABLE _user DROP COLUMN role;
    ALTER TABLE _user 
      ADD COLUMN role_id int8,
      ADD CONSTRAINT user_role_fkey FOREIGN KEY (role_id) REFERENCES role (id);
  END IF;

  -- ADD FIRST NAME
  IF NOT EXISTS (
      SELECT 1
      FROM information_schema.columns
      WHERE table_schema = 'chat'
        AND table_name = '_user'
        AND column_name = 'first_name'
  ) THEN
    ALTER TABLE _user ADD COLUMN first_name text NOT NULL;
  END IF;

  -- ADD LAST NAME
  IF NOT EXISTS (
      SELECT 1
      FROM information_schema.columns
      WHERE table_schema = 'chat'
        AND table_name = '_user'
        AND column_name = 'last_name'
  ) THEN
    ALTER TABLE _user ADD COLUMN last_name text;
  END IF;

  -- ADD IS_ENABLED
  IF EXISTS (
    SELECT 1
      FROM information_schema.columns
      WHERE table_schema = 'chat'
        AND table_name = '_user'
        AND column_name = 'is_active'
  ) THEN
    ALTER TABLE _user RENAME COLUMN is_active TO is_enabled;
  END IF;

  -- ADD IS_CREATION_COMPLETED
  IF NOT EXISTS (
    SELECT 1
      FROM information_schema.columns
      WHERE table_schema = 'chat'
        AND table_name = '_user'
        AND column_name = 'is_creation_completed'
  ) THEN
    ALTER TABLE _user ADD COLUMN is_creation_completed DEFAULT TRUE;
  END IF;

  -- ADD KEYCLOAK_ID
  IF NOT EXISTS (
    SELECT 1
      FROM information_schema.columns
      WHERE table_schema = 'chat'
        AND table_name = '_user'
        AND column_name = 'keycloak_id'
  ) THEN
    ALTER TABLE _user ADD COLUMN keycloak_id UUID;
  END IF;

END $$;