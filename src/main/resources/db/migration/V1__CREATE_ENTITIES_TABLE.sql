-- Admins Table
CREATE TABLE IF NOT EXISTS admins
(
    person_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    contact     VARCHAR(255) NOT NULL UNIQUE,
    street      VARCHAR(255) NOT NULL,
    city        VARCHAR(255) NOT NULL,
    state       VARCHAR(255) NOT NULL,
    country     VARCHAR(255) NOT NULL,
    postal_code VARCHAR(255) NOT NULL,
    admin_code  VARCHAR(255) NOT NULL UNIQUE,
    role        VARCHAR(255) NOT NULL
);

-- Librarians Table
CREATE TABLE IF NOT EXISTS librarians
(
    person_id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE,
    contact       VARCHAR(255) NOT NULL UNIQUE,
    street        VARCHAR(255) NOT NULL,
    city          VARCHAR(255) NOT NULL,
    state         VARCHAR(255) NOT NULL,
    country       VARCHAR(255) NOT NULL,
    postal_code   VARCHAR(255) NOT NULL,
    employee_code VARCHAR(255) NOT NULL UNIQUE,
    admin_id      BIGINT,
    FOREIGN KEY (admin_id) REFERENCES admins (person_id) ON DELETE SET NULL
);

-- Members Table
CREATE TABLE IF NOT EXISTS members
(
    person_id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    email             VARCHAR(255) NOT NULL UNIQUE,
    contact           VARCHAR(255) NOT NULL UNIQUE,
    street            VARCHAR(255) NOT NULL,
    city              VARCHAR(255) NOT NULL,
    state             VARCHAR(255) NOT NULL,
    country           VARCHAR(255) NOT NULL,
    postal_code       VARCHAR(255) NOT NULL,
    membership_status VARCHAR(255) NOT NULL,
    librarian_id      BIGINT,
    admin_id          BIGINT,
    FOREIGN KEY (librarian_id) REFERENCES librarians (person_id) ON DELETE SET NULL,
    FOREIGN KEY (admin_id) REFERENCES admins (person_id) ON DELETE SET NULL
);

-- Books Table
CREATE TABLE IF NOT EXISTS books
(
    book_id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title          VARCHAR(255) NOT NULL,
    author         VARCHAR(255) NOT NULL,
    isbn           VARCHAR(255) NOT NULL UNIQUE,
    genre          VARCHAR(255),
    publisher_name VARCHAR(255),
    publisher_year INT,
    book_status    VARCHAR(255) NOT NULL,
    member_id      BIGINT,
    librarian_id   BIGINT,
    FOREIGN KEY (member_id) REFERENCES members (person_id) ON DELETE CASCADE,
    FOREIGN KEY (librarian_id) REFERENCES librarians (person_id) ON DELETE SET NULL
);

-- Fine Table
CREATE TABLE IF NOT EXISTS fines
(
    fine_id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    amount       DECIMAL(10, 2) NOT NULL,
    issue_date   DATE           NOT NULL,
    due_date     DATE           NOT NULL,
    member_id    BIGINT         NOT NULL,
    librarian_id BIGINT,
    admin_id     BIGINT,
    FOREIGN KEY (member_id) REFERENCES members (person_id) ON DELETE CASCADE,
    FOREIGN KEY (librarian_id) REFERENCES librarians (person_id) ON DELETE SET NULL,
    FOREIGN KEY (admin_id) REFERENCES admins (person_id) ON DELETE SET NULL
);

-- Member_Books Table (Many-to-Many relationship between Members and Books)
CREATE TABLE IF NOT EXISTS member_books
(
    member_id BIGINT NOT NULL,
    book_id   BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES members (person_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books (book_id) ON DELETE CASCADE,
    PRIMARY KEY (member_id, book_id)
);
