INSERT INTO users (email, password, role, created_at)
     VALUES('admin@gmail.com', '$2a$10$btMaeAlg7Q2s8prHN.sUu.R0ZgDEkQ125csQ3tFvefFHD2McVuzQm',
             'ADMIN',current_timestamp);

INSERT INTO contact_types (type_value, created_at, modified_at)
     VALUES ('FAMILY', current_timestamp, current_timestamp),
	 		('FRIEND', current_timestamp, current_timestamp),
	 		('WORK',   current_timestamp, current_timestamp);