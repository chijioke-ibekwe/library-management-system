CREATE TABLE permissions (
    id                      BIGSERIAL NOT NULL,
    created_at              TIMESTAMP NOT NULL,
    updated_at              TIMESTAMP,
    description             VARCHAR(255),
    name                    VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE permission_role (
    role_id         INT8 NOT NULL,
    permission_id   INT8 NOT NULL,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE roles (
    id              BIGSERIAL NOT NULL,
    created_at      TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP,
    description     VARCHAR(255),
    name            VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE users (
    id                                  BIGSERIAL NOT NULL,
    created_at                          TIMESTAMP NOT NULL,
    updated_at                          TIMESTAMP,
    first_name                          VARCHAR(255) NOT NULL,
    last_name                           VARCHAR(255) NOT NULL,
    username                            VARCHAR(255) NOT NULL,
    password                            VARCHAR(255),
    phone_number                        VARCHAR(255),
    account_non_expired                 BOOLEAN DEFAULT TRUE,
    account_non_locked                  BOOLEAN DEFAULT TRUE,
    credentials_non_expired             BOOLEAN DEFAULT TRUE,
    enabled                             BOOLEAN DEFAULT TRUE,
    role_id                             INT8 NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE books (
    id                                  BIGSERIAL NOT NULL,
    created_at                          TIMESTAMP NOT NULL,
    updated_at                          TIMESTAMP,
    title                               VARCHAR(255) NOT NULL,
    author                              VARCHAR(255) NOT NULL,
    publication_year                    DATE NOT NULL,
    isbn                                VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE patrons (
    id                                  BIGSERIAL NOT NULL,
    created_at                          TIMESTAMP NOT NULL,
    updated_at                          TIMESTAMP,
    first_name                          VARCHAR(255) NOT NULL,
    last_name                           VARCHAR(255) NOT NULL,
    email                               VARCHAR(255),
    phone_number                        VARCHAR(255) NOT NULL,
    street_address                      VARCHAR(255),
    city                                VARCHAR(255),
    state                               VARCHAR(255),
    country                             VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE borrowing_records (
    id                                  BIGSERIAL NOT NULL,
    created_at                          TIMESTAMP NOT NULL,
    updated_at                          TIMESTAMP,
    borrowing_date                      DATE NOT NULL,
    return_date                         DATE NOT NULL,
    book_id                             INT8 NOT NULL,
    patron_id                           INT8 NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE users ADD CONSTRAINT uk_users_username unique (username);

ALTER TABLE permission_role ADD CONSTRAINT fk_permission_role_permissions FOREIGN KEY (permission_id) REFERENCES permissions (id);

ALTER TABLE permission_role ADD CONSTRAINT fk_permission_role_roles FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE users ADD CONSTRAINT fk_users_roles FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE borrowing_records ADD CONSTRAINT fk_borrowing_records_books FOREIGN KEY (book_id) REFERENCES books (id) ON DELETE CASCADE;

ALTER TABLE borrowing_records ADD CONSTRAINT fk_borrowing_records_patrons FOREIGN KEY (patron_id) REFERENCES patrons (id) ON DELETE CASCADE;

CREATE INDEX idx_username_users ON users(username);

CREATE INDEX idx_name_roles ON roles(name);