-- Insert Members with librarian_id and admin_id referring to the librarians and admins tables
-- Assume librarians and admins are inserted with person_id 1 and 2

INSERT INTO members (person_id, name, email, contact, street, city, state, country, postal_code, membership_status,
                     librarian_id, admin_id)
VALUES (nextval('person_id_seq'), 'Charlie Davis', 'charlie.davis@example.com', '2233445566', '202 Birch St',
        'Springfield', 'IL', 'USA', '62701', 'ACTIVE', 3, 1),
       (nextval('person_id_seq'), 'Diana Evans', 'diana.evans@example.com', '3344556677', '303 Cedar St', 'Shelbyville',
        'IL', 'USA', '62565', 'ACTIVE', 4, 2);
-- Insert enum values into statuses table
INSERT INTO statuses (book_status)
VALUES ('Available'),
       ('borrowed'),
       ('loaned'),
       ('lost'),
       ('Reserved');


-- Insert books with corresponding status_id
-- Assume status_id for 'Available' is 1, 'borrowed' is 2, etc.

INSERT INTO books (title, author, isbn, genre, publisher_name, publisher_year, status_id, librarian_id)
VALUES ('Book Title 1', 'Author A', 'ISBN0001', 'Fiction', 'Publisher X', 2023, 1, 3), -- status_id 1 for AVAILABLE
       ('Book Title 2', 'Author B', 'ISBN0002', 'Non-Fiction', 'Publisher Y', 2022, 2, 4);
-- status_id 2 for BORROWED


-- Insert Member-Book relationships (Many-to-Many)
-- Assume members with person_id 1 and 2, and books with book_id 1 and 2

INSERT INTO member_books (member_id, book_id)
VALUES (9, 1),
       (10, 2);

INSERT INTO fines (fine_id, amount, issue_date, due_date, member_id, librarian_id, admin_id)
VALUES (nextval('fine_id_seq'), 50.00, '2024-01-01', '2024-01-15', 9, 3, 1),
       (nextval('fine_id_seq'), 75.00, '2024-02-01', '2024-02-15', 10, 4, 2) ;