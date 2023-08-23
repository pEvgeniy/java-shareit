DROP ALL OBJECTS;

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    email   VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS item_requests (
    item_request_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description     VARCHAR(255) NOT NULL,
    requester_id    INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    created_at      TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS items (
    item_id         INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(255) NOT NULL,
    is_available    BOOLEAN NOT NULL,
    owner_id        INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    item_request_id INTEGER REFERENCES item_requests(item_request_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS booking (
    booking_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_at   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_at     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id    INTEGER REFERENCES items(item_id) ON DELETE CASCADE,
    booker_id  INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    status     VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS comments (
    comment_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    text       VARCHAR(255) NOT NULL,
    item_id    INTEGER REFERENCES items(item_id) ON DELETE CASCADE,
    author_id  INTEGER REFERENCES users(user_id) ON DELETE CASCADE,
    created_at TIMESTAMP WITHOUT TIME ZONE
);