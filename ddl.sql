CREATE TABLE IF NOT EXISTS user_info (
    user_no     INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id     TEXT    UNIQUE,
    user_name   TEXT    NOT NULL,
    password    TEXT    NOT NULL
);
