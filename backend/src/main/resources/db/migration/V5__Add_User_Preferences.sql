-- Create the user_preferences table
CREATE TABLE user_preferences (
    user_id UUID NOT NULL,
    missing_daily_memory_reminder_time TIME,
    CONSTRAINT fk_user_preferences_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);
