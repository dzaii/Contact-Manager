CREATE DOMAIN phone AS text CHECK(VALUE ~ '^\+\d{8,18}$');

ALTER TABLE users ADD COLUMN phone_number phone UNIQUE;
ALTER TABLE users ADD COLUMN enabled BOOLEAN    NOT NULL DEFAULT false;
     UPDATE users        SET enabled = true;