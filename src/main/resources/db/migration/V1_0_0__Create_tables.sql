CREATE TABLE users (
	PRIMARY KEY (id),
	id            serial,
	first_name    varchar (30),
	last_name     varchar (30),
	email         varchar (124) UNIQUE,
	password      varchar (255),
	role          varchar (10),
	created_at    timestamp,
	modified_at   timestamp
);

CREATE TABLE contact_types (
	PRIMARY KEY (id),
	id          serial,
	type_value  varchar (20) UNIQUE,
	created_at  timestamp,
    modified_at timestamp
);

CREATE TABLE contacts (
	PRIMARY KEY (id),
	id           serial,
	first_name   varchar(30),
	last_name    varchar(30),
	email        varchar(124),
	phone_number varchar(25),
	created_at   timestamp,
	modified_at  timestamp,
	user_id      int 		  REFERENCES users (id),
	type_id      int          REFERENCES contact_types (id)
);