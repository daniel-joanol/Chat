INSERT INTO _user (
  username,
  email,
  first_name,
  last_name,
  created_at,
  status,
  type,
  role_id,
  is_enabled,
  is_creation_completed,
  keycloak_id
)
VALUES (
  'admin',
  'admin@chat.local',
  'System',
  'Admin',
  NOW(),
  'OFFLINE',
  'INTERNAL',
  (SELECT id FROM role WHERE name = 'ADMIN' LIMIT 1),
  TRUE,
  TRUE,
  NULL
)
ON CONFLICT (username) DO UPDATE
SET email = EXCLUDED.email,
    first_name = EXCLUDED.first_name,
    last_name = EXCLUDED.last_name,
    status = EXCLUDED.status,
    type = EXCLUDED.type,
    role_id = EXCLUDED.role_id,
    is_enabled = EXCLUDED.is_enabled,
    is_creation_completed = EXCLUDED.is_creation_completed,
    keycloak_id = COALESCE(_user.keycloak_id, EXCLUDED.keycloak_id);
