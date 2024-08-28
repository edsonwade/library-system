INSERT INTO books (book_id, title, author, isbn, genre, publisher_name, publisher_year, book_status, member_id ,librarian_id)
VALUES (nextval('book_id_seq'), 'Book Title 1', 'Author A', 'ISBN0001', 'Fiction', 'Publisher X', 2023,'Available',5,3),
       (nextval('book_id_seq'), 'Book Title 2', 'Author B', 'ISBN0002', 'Non-Fiction', 'Publisher Y', 2022,'borrowed',6, 4);