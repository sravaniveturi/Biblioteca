CREATE TABLE IF NOT EXISTS users (
    id int,
    firstName varchar(100) NOT NULL,
    lastName varchar(100),
    email varchar(100) NOT NULL,
    password varchar(100) NOT NULL,
    PRIMARY KEY (id)
);