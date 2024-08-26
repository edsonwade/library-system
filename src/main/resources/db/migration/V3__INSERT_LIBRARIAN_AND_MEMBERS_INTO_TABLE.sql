INSERT INTO librarians (person_id, name, email, contact, street, city, state, country, postal_code, employee_code,
                        admin_id)
VALUES (nextval('person_id_seq'), 'Alicia Johnson', 'alicia.johnson@example.com', '1112233445', '789 Pine St',
        'Springfield', 'IL', 'USA', '62701', 'EMP001', 1),
       (nextval('person_id_seq'), 'Bo Brown', 'bo.brown@example.com', '5556677889', '101 Maple St', 'Shelbyville',
        'IL', 'USA', '62565', 'EMP002', 2);

INSERT INTO members (person_id, name, email, contact, street, city, state, country, postal_code, membership_status,
                     librarian_id, admin_id)
VALUES (nextval('person_id_seq'), 'Charlie Davis', 'charlie.davis@example.com', '2233445566', '202 Birch St',
        'Springfield', 'IL', 'USA', '62701', 'ACTIVE', 3, 1),
       (nextval('person_id_seq'), 'Diana Evans', 'diana.evans@example.com', '3344556677', '303 Cedar St', 'Shelbyville',
        'IL', 'USA', '62565', 'ACTIVE', 4, 2);

INSERT INTO statuses (book_status)
VALUES ('Available'),
       ('borrowed'),
       ('loaned'),
       ('lost'),
       ('Reserved');
