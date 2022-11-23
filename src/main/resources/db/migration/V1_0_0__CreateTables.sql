CREATE TABLE users (
	PRIMARY KEY (user_id),
	user_id       serial,
	first_name    varchar (20),
	last_name     varchar (20),
	email         varchar (50) UNIQUE,
	user_password varchar (50),
	user_role     varchar (10),
	created_at    timestamp,
	modified_at   timestamp
);

CREATE TABLE contact_types (
	PRIMARY KEY (type_id),
	type_id       serial,
	type_value    varchar (20)
);

CREATE TABLE contacts (
	PRIMARY KEY (contact_id),
	contact_id   serial,
	first_name   varchar(20),
	last_name    varchar(20),
	email        varchar(50),
	phone_number varchar(50),
	created_at   timestamp,
	modified_at  timestamp,
	user_id      int 		  REFERENCES users (user_id),
	type_id      int NOT NULL REFERENCES contact_types (type_id)
);