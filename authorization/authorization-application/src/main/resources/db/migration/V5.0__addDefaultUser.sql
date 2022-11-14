-- Пароль - passwordfortoolkit
INSERT INTO users(id, username, password, email)
VALUES ('a487f7fa-e658-11eb-ba80-0242ac130004', 'ADMIN', '$2y$12$Usgx2M5qDL50po6IVbqlhOAxxVHeYGzBpAnbvEb2ZQazfSaqDjti2',
        'atest@gmail.com');
INSERT INTO user_roles(user_id, role_id)
VALUES  ('a487f7fa-e658-11eb-ba80-0242ac130004', '2')


