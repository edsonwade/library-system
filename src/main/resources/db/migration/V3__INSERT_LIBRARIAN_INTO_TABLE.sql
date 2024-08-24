INSERT INTO librarians (person_id, name, email, contact, street, city, state, country, postal_code, employee_code,
                        admin_id)
VALUES (nextval('person_id_seq'), 'Alicia Johnson', 'alicia.johnson@example.com', '1112233445', '789 Pine St',
        'Springfield', 'IL', 'USA', '62701', 'EMP001', 1),
       (nextval('person_id_seq'), 'Bo Brown', 'bo.brown@example.com', '5556677889', '101 Maple St', 'Shelbyville',
        'IL', 'USA', '62565', 'EMP002', 2);