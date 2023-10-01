CREATE TABLE "user"
(
    id       BIGSERIAL PRIMARY KEY,
    email    VARCHAR UNIQUE NOT NULL,
    password VARCHAR        NOT NULL
);

CREATE INDEX ON "user" (email);

CREATE TABLE "role"
(
    id   BIGSERIAL PRIMARY KEY NOT NULL,
    name VARCHAR UNIQUE        NOT NULL
);

CREATE TABLE user_role
(
    id      BIGSERIAL PRIMARY KEY         NOT NULL,
    user_id BIGINT REFERENCES "user" (id) NOT NULL,
    role_id BIGINT REFERENCES "role" (id) NOT NULL,
    UNIQUE (user_id, role_id)
);

CREATE INDEX ON "user_role" (user_id);
CREATE INDEX ON "user_role" (role_id);

CREATE TABLE "city"
(
    id    BIGSERIAL PRIMARY KEY,
    name  VARCHAR,
    photo VARCHAR NOT NULL
);

CREATE INDEX ON "city" (name);
