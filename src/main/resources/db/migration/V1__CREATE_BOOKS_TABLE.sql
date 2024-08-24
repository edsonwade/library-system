-- Create sequence if it does not exist
CREATE SEQUENCE IF NOT EXISTS book_id_seq;

-- Create table if it does not exist
CREATE TABLE IF NOT EXISTS books
(
    book_id             INT PRIMARY KEY DEFAULT nextval('book_id_seq'),
    title               VARCHAR(255)        NOT NULL,
    author              VARCHAR(255)        NOT NULL,
    isbn                VARCHAR(255) UNIQUE NOT NULL,
    genre               VARCHAR(255)        NOT NULL,
    publisher_name      VARCHAR(255)        NOT NULL,
    publisher_year      INT                 NOT NULL,
    book_status VARCHAR(255) NOT NULL
);
