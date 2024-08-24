INSERT INTO admins (person_id, name, email, contact, street, city, state, country, postal_code, admin_code, role)
VALUES (nextval('person_id_seq'), 'Sonia Doe', 'sonia.doe@example.com', '1234567890', '123 Elm St', 'Springfield', 'IL',
        'USA', '62701', 'SUPAD234', 'SUPER_ADMIN'),
       (nextval('person_id_seq'), 'rau Smith', 'rau.smith@example.com', '0987654321', '456 Oak St', 'Shelbyville',
        'IL', 'USA', '62565', 'SYSA124', 'SYSTEM_ADMIN');
