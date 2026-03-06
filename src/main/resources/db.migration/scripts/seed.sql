-- liquibase formatted sql

-- changeset insert test data:3

INSERT INTO users (username, password, role) VALUES (
    'test_user_1',
    '24fb54e91c95e45891800d8d81af22090704f52161d2512e7002a8048a35fb019956905c6ef231e9cffdabfb28b96251',
    'USER'
);

INSERT INTO users (username, password, role) VALUES (
    'test_admin_1',
    '4972caa375ddc73de800ac2d6ea4a883bf91ca04153756c06b7154eabdf84bb9b8f23709862fb431baa61bd112ab8db7',
    'ADMIN'
);

INSERT INTO cards (number, masked_number, expired_at, status, balance, user_id) VALUES (
    '/2a+eowb2Waj9BoUGNHQqq8E7dOcEC4OV8McnVKa26I=',
    '**** **** **** 4619',
    '2036-03-04',
    'ACTIVE', 
    1000.00, 
    1
);
