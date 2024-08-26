INSERT INTO member_books (member_id, book_id)
VALUES (5, 1),
       (6, 2);

INSERT INTO fines (fine_id, amount, issue_date, due_date, member_id, librarian_id, admin_id)
VALUES (nextval('fine_id_seq'), 50.00, '2024-01-01', '2024-01-15', 5, 3, 1),
       (nextval('fine_id_seq'), 75.00, '2024-02-01', '2024-02-15', 6, 4, 2);