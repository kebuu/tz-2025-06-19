-- Add user_id column as nullable initially
ALTER TABLE memory ADD COLUMN user_id UUID;

-- Set a default user_id for existing records (using the first user in the app_user table)
UPDATE memory SET user_id = (SELECT id FROM app_user LIMIT 1);

-- Make user_id column NOT NULL after setting default values
ALTER TABLE memory ALTER COLUMN user_id SET NOT NULL;
