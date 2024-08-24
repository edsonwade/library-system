CREATE SEQUENCE IF NOT EXISTS person_id_seq;

CREATE TABLE IF NOT EXISTS members
(
    person_id         BIGINT PRIMARY KEY DEFAULT nextval('person_id_seq'),
    name              VARCHAR(255) NOT NULL,
    email             VARCHAR(255) NOT NULL UNIQUE,
    contact           VARCHAR(255) NOT NULL UNIQUE,
    street            VARCHAR(255) NOT NULL,
    city              VARCHAR(255) NOT NULL,
    state             VARCHAR(255) NOT NULL,
    country           VARCHAR(255) NOT NULL,
    postal_code       VARCHAR(255) NOT NULL,
    membership_status VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS member_books
(
    member_id BIGINT NOT NULL,
    book_id   BIGINT NOT NULL,
    PRIMARY KEY (member_id, book_id),
    FOREIGN KEY (member_id) REFERENCES members (person_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (book_id) ON DELETE CASCADE
);
