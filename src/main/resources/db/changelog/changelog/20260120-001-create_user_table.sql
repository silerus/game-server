--liquibase formatted sql

--changeset dev_user:1
CREATE EXTENSION IF NOT EXISTS citext;
--rollback DROP EXTENSION IF EXISTS citext;

--changeset dev_user:2
CREATE TYPE user_role AS ENUM ('USER', 'ADMIN');
--rollback DROP TYPE IF EXISTS user_role;

--changeset dev_user:3
CREATE TABLE IF NOT EXISTS users
(
    id            UUID        NOT NULL PRIMARY KEY,
    email         citext      NOT NULL UNIQUE,
    password_hash TEXT        NOT NULL,
    role          user_role   NOT NULL,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
--rollback DROP TABLE IF EXISTS users;

--changeset dev_user:4 splitStatements:false endDelimiter:@@
CREATE OR REPLACE FUNCTION trigger_set_updated_at()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
@@
--rollback DROP FUNCTION IF EXISTS trigger_set_updated_at();

--changeset dev_user:5
CREATE OR REPLACE TRIGGER users_set_updated_at
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION trigger_set_updated_at();
--rollback DROP TRIGGER IF EXISTS users_set_updated_at ON users;
