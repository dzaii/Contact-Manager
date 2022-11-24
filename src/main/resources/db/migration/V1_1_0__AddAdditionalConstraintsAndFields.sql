ALTER TABLE users ALTER COLUMN email      SET  NOT NULL;
ALTER TABLE users ALTER COLUMN password   SET  NOT NULL;
ALTER TABLE users ALTER COLUMN role       SET  NOT NULL;
ALTER TABLE users ALTER COLUMN created_at SET  NOT NULL;

ALTER TABLE contact_types ALTER COLUMN type_value SET NOT NULL;
ALTER TABLE contact_types ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE contacts ALTER COLUMN user_id    SET NOT NULL;
ALTER TABLE contacts ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE contacts ADD CONSTRAINT at_least_one_present
                     CHECK (COALESCE(first_name, last_name, email, phone_number) IS NOT NULL);

ALTER TABLE contacts
ADD COLUMN address varchar(100),
ADD COLUMN info    varchar(100);