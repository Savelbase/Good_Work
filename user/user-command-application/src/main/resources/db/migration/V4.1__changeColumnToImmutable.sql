ALTER TABLE role DROP COLUMN IF EXISTS deletable;
ALTER TABLE role ADD COLUMN IF NOT EXISTS immutable bool NOT NULL DEFAULT false;