INSERT INTO permissions (created_at, name, description) VALUES (
CURRENT_TIMESTAMP,
'users:read',
'Permission to fetch all users'
);

INSERT INTO roles (created_at, name, description) VALUES (CURRENT_TIMESTAMP, 'role_user', 'Role for users on the library management application');

INSERT INTO permission_role (role_id, permission_id) VALUES (
(SELECT id FROM roles WHERE name = 'role_user'),
(SELECT id FROM permissions WHERE name = 'users:read')
);