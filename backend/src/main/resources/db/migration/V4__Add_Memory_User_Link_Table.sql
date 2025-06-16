CREATE TABLE memory_user_link (
    memory_id UUID NOT NULL,
    user_id UUID NOT NULL,
    user_can_access BOOLEAN NOT NULL,
    PRIMARY KEY (memory_id, user_id),
    FOREIGN KEY (memory_id) REFERENCES memory(id),
    FOREIGN KEY (user_id) REFERENCES app_user(id)
);
