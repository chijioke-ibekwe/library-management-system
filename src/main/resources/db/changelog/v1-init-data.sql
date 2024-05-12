INSERT INTO permissions (created_at, name, description) VALUES (
CURRENT_TIMESTAMP,
'users:read',
'Permission to fetch users'
);

INSERT INTO permissions (created_at, name, description) VALUES (
CURRENT_TIMESTAMP,
'books:read',
'Permission to fetch books'
);

INSERT INTO permissions (created_at, name, description) VALUES (
CURRENT_TIMESTAMP,
'books:write',
'Permission to create/update books'
);

INSERT INTO permissions (created_at, name, description) VALUES (
CURRENT_TIMESTAMP,
'books:delete',
'Permission to delete books'
);

INSERT INTO permissions (created_at, name, description) VALUES (
CURRENT_TIMESTAMP,
'patrons:read',
'Permission to fetch patrons'
);

INSERT INTO permissions (created_at, name, description) VALUES (
CURRENT_TIMESTAMP,
'patrons:write',
'Permission to create/update patrons'
);

INSERT INTO permissions (created_at, name, description) VALUES (
CURRENT_TIMESTAMP,
'patrons:delete',
'Permission to delete patrons'
);

INSERT INTO permissions (created_at, name, description) VALUES (
CURRENT_TIMESTAMP,
'borrowing_records:write',
'Permission to create/update borrowing records'
);

INSERT INTO roles (created_at, name, description) VALUES (CURRENT_TIMESTAMP, 'role_user', 'Role for users on the library management application');

INSERT INTO permission_role (role_id, permission_id) VALUES (
(SELECT id FROM roles WHERE name = 'role_user'),
(SELECT id FROM permissions WHERE name = 'users:read')
);

INSERT INTO permission_role (role_id, permission_id) VALUES (
(SELECT id FROM roles WHERE name = 'role_user'),
(SELECT id FROM permissions WHERE name = 'books:read')
);

INSERT INTO permission_role (role_id, permission_id) VALUES (
(SELECT id FROM roles WHERE name = 'role_user'),
(SELECT id FROM permissions WHERE name = 'books:write')
);

INSERT INTO permission_role (role_id, permission_id) VALUES (
(SELECT id FROM roles WHERE name = 'role_user'),
(SELECT id FROM permissions WHERE name = 'books:delete')
);

INSERT INTO permission_role (role_id, permission_id) VALUES (
(SELECT id FROM roles WHERE name = 'role_user'),
(SELECT id FROM permissions WHERE name = 'patrons:read')
);

INSERT INTO permission_role (role_id, permission_id) VALUES (
(SELECT id FROM roles WHERE name = 'role_user'),
(SELECT id FROM permissions WHERE name = 'patrons:write')
);

INSERT INTO permission_role (role_id, permission_id) VALUES (
(SELECT id FROM roles WHERE name = 'role_user'),
(SELECT id FROM permissions WHERE name = 'patrons:delete')
);

INSERT INTO permission_role (role_id, permission_id) VALUES (
(SELECT id FROM roles WHERE name = 'role_user'),
(SELECT id FROM permissions WHERE name = 'borrowing_records:write')
);