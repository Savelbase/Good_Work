-- Удаляем тестовые встречи и накатываем заново
DELETE
FROM one_to_one_command
WHERE id IN ('d3a04c02-db8b-4a99-a9d9-96a6efc99563',
             '720b3a2c-bc22-4e52-8939-fb7d77b2912f',
             'f148b72a-6b61-4873-867d-efa937a23551',
             '4384465c-169c-4bdb-aadd-76b103079dc2',
             '04628bc8-a272-46d8-9fcd-4fb3e4a0340d',
             '857e3120-720e-11ec-90d6-0242ac120003',
             'c085f3f6-689b-11ec-90d6-0242ac120003',
             'e482614a-689b-11ec-90d6-0242ac120003',
             '384f4ebe-689c-11ec-90d6-0242ac120003',
             '3f4bc742-689c-11ec-90d6-0242ac120003',
             '4be62344-689c-11ec-90d6-0242ac120003',
             '5183cd42-689c-11ec-90d6-0242ac120003',
             'd2e14466-7209-11ec-90d6-0242ac120003');

DELETE
FROM users
WHERE id IN ('49877fc8-d9d5-479b-ad4f-6ebe5d613dc9',
             '2d6c34cd-8ed1-46cd-a6c2-d360a602c657',
             '7368017b-f0d5-4e54-bbe4-72144e4d20e4',
             'dde4e690-c634-4991-a751-5af71d3bdf70',
             'ee9c12c2-ac2b-4432-ac55-d0fdc764af37',
             '33f0e4e0-0f16-4f7a-83a0-633daa763557',
             '4c353585-be59-41a2-9e36-edc7a0b57b3c',
             'f92e2865-fa9d-4115-a455-c257609a7ffc',
             '0967a0bd-a9e7-4d53-a396-5a0d6b00bd4c',
             '3bf804ba-9d2b-4e30-9326-bb3a040425e1',
             '0221140f-671e-4de7-b485-4a31f9b7b2e4',
             'ede95340-3219-4d2d-8010-1d1ceaa9ddd0',
             'afa5a87b-1cab-4844-b7cf-fdcf8cc17fe3',
             '48b0204f-65b5-4940-81a9-64a55d671c2a',

             'e45bfff0-1b04-4b8a-ac95-9629dff88a3e',

             '84be7ac5-e56c-40a2-b21a-655aa74e289c',
             '0922737d-2b19-456b-949b-15740eea99d3',
             '7fbdcccb-f166-49af-ac63-a14d87a0d914',
             '9bdb3b40-5d24-4027-8313-ff633977a401',
             'def5cc13-cd01-483c-916d-476ec22cdc68',
             'db9a9958-79f3-40ab-8fbd-97dd110c4a4e',
             '9645183c-8834-434f-be85-336d68fdbee9',
             'c3f30385-56b9-4457-ab2a-b5d9fb13b2d3',
             '3515fcc1-477d-4b86-a269-8be11125e13b',
             'daca41f1-ce5c-4045-a957-e68bef954fbe',
             '6a9fae7e-b659-4fe1-b979-323d363b3de9',
             '8b690fa4-f5ae-4dc7-a6c4-43332a215075',
             '80bcad3f-429e-49a8-ad2f-02706cd5f0ba',
             '4c0f167d-73a4-4fa1-8e73-7dca2947d655',

             '5b3ec5ae-314f-44fb-b809-8d808fdcfcca',
             '07d756a2-97cf-4ebf-8727-8d68f5e659ff',
             '5dd387cb-85dc-4165-a719-6f48198423e8',
             '42e28c4c-3ee9-4e2b-8c61-425ee6342463',
             '26db13ab-c76f-4f76-ae57-a9770a46e8ed',
             '8f754c34-295f-4319-a6b6-aecf6212b8b7',
             '173c378a-471d-4cac-a989-d5316d88bce3',
             '88b6958f-0ba8-4e31-add0-8dd43b6ccf0d',
             '0e3ca8f3-19c7-4c7c-b5e4-c545e968069b',
             'f77d19da-2eac-460f-bbac-082de4ba54f4',
             '7a2e15bd-8627-4812-a621-7fffd597c98b',
             'edddc6c3-2c35-4a81-a3c3-7684c75f72fa',
             '2b20e3a7-d5f5-4183-b59e-7a61c4d975c9',
             'a03a3468-e413-4640-a252-3f4ca337c0b1',

             '2f4e97a5-e631-4578-b570-0d656f2bbd5b',
             'e1a04fc6-8110-446d-8832-877f4dfe11f4',
             '7ff17452-d42a-4d69-b3d9-ad0288c666c3',
             '9d567cb3-d7b6-4339-afd9-3a8efa2ec121',
             'd1467e23-8d55-460b-b214-0a42bffa48f2');

-- Отдел Administrations
INSERT INTO users(id, resource_manager_id)
VALUES ('e45bfff0-1b04-4b8a-ac95-9629dff88a3e', null);

-- Отдел Development
INSERT INTO users(id, resource_manager_id)
VALUES ('84be7ac5-e56c-40a2-b21a-655aa74e289c', 'e45bfff0-1b04-4b8a-ac95-9629dff88a3e'),
       ('0922737d-2b19-456b-949b-15740eea99d3', '84be7ac5-e56c-40a2-b21a-655aa74e289c'),
       ('7fbdcccb-f166-49af-ac63-a14d87a0d914', '0922737d-2b19-456b-949b-15740eea99d3'),
       ('9bdb3b40-5d24-4027-8313-ff633977a401', '0922737d-2b19-456b-949b-15740eea99d3'),
       ('def5cc13-cd01-483c-916d-476ec22cdc68', '7fbdcccb-f166-49af-ac63-a14d87a0d914'),
       ('db9a9958-79f3-40ab-8fbd-97dd110c4a4e', '7fbdcccb-f166-49af-ac63-a14d87a0d914'),
       ('9645183c-8834-434f-be85-336d68fdbee9', '7fbdcccb-f166-49af-ac63-a14d87a0d914'),
       ('c3f30385-56b9-4457-ab2a-b5d9fb13b2d3', '7fbdcccb-f166-49af-ac63-a14d87a0d914'),
       ('3515fcc1-477d-4b86-a269-8be11125e13b', 'def5cc13-cd01-483c-916d-476ec22cdc68'),
       ('daca41f1-ce5c-4045-a957-e68bef954fbe', 'def5cc13-cd01-483c-916d-476ec22cdc68'),
       ('6a9fae7e-b659-4fe1-b979-323d363b3de9', 'db9a9958-79f3-40ab-8fbd-97dd110c4a4e'),
       ('8b690fa4-f5ae-4dc7-a6c4-43332a215075', 'db9a9958-79f3-40ab-8fbd-97dd110c4a4e'),
       ('80bcad3f-429e-49a8-ad2f-02706cd5f0ba', '9bdb3b40-5d24-4027-8313-ff633977a401'),
       ('4c0f167d-73a4-4fa1-8e73-7dca2947d655', '9bdb3b40-5d24-4027-8313-ff633977a401');

-- Отдел QA
INSERT INTO users(id, resource_manager_id)
VALUES ('49877fc8-d9d5-479b-ad4f-6ebe5d613dc9', 'e45bfff0-1b04-4b8a-ac95-9629dff88a3e'),
       ('2d6c34cd-8ed1-46cd-a6c2-d360a602c657', '49877fc8-d9d5-479b-ad4f-6ebe5d613dc9'),
       ('7368017b-f0d5-4e54-bbe4-72144e4d20e4', '2d6c34cd-8ed1-46cd-a6c2-d360a602c657'),
       ('dde4e690-c634-4991-a751-5af71d3bdf70', '2d6c34cd-8ed1-46cd-a6c2-d360a602c657'),
       ('ee9c12c2-ac2b-4432-ac55-d0fdc764af37', '7368017b-f0d5-4e54-bbe4-72144e4d20e4'),
       ('33f0e4e0-0f16-4f7a-83a0-633daa763557', '7368017b-f0d5-4e54-bbe4-72144e4d20e4'),
       ('4c353585-be59-41a2-9e36-edc7a0b57b3c', '7368017b-f0d5-4e54-bbe4-72144e4d20e4'),
       ('f92e2865-fa9d-4115-a455-c257609a7ffc', '7368017b-f0d5-4e54-bbe4-72144e4d20e4'),
       ('0967a0bd-a9e7-4d53-a396-5a0d6b00bd4c', 'ee9c12c2-ac2b-4432-ac55-d0fdc764af37'),
       ('3bf804ba-9d2b-4e30-9326-bb3a040425e1', 'ee9c12c2-ac2b-4432-ac55-d0fdc764af37'),
       ('0221140f-671e-4de7-b485-4a31f9b7b2e4', '33f0e4e0-0f16-4f7a-83a0-633daa763557'),
       ('ede95340-3219-4d2d-8010-1d1ceaa9ddd0', '33f0e4e0-0f16-4f7a-83a0-633daa763557'),
       ('afa5a87b-1cab-4844-b7cf-fdcf8cc17fe3', 'dde4e690-c634-4991-a751-5af71d3bdf70'),
       ('48b0204f-65b5-4940-81a9-64a55d671c2a', 'dde4e690-c634-4991-a751-5af71d3bdf70');

-- Отдел Bookkeeping
INSERT INTO users(id, resource_manager_id)
VALUES ('5b3ec5ae-314f-44fb-b809-8d808fdcfcca', 'e45bfff0-1b04-4b8a-ac95-9629dff88a3e'),
       ('07d756a2-97cf-4ebf-8727-8d68f5e659ff', '5b3ec5ae-314f-44fb-b809-8d808fdcfcca'),
       ('5dd387cb-85dc-4165-a719-6f48198423e8', '07d756a2-97cf-4ebf-8727-8d68f5e659ff'),
       ('42e28c4c-3ee9-4e2b-8c61-425ee6342463', '07d756a2-97cf-4ebf-8727-8d68f5e659ff'),
       ('26db13ab-c76f-4f76-ae57-a9770a46e8ed', '5dd387cb-85dc-4165-a719-6f48198423e8'),
       ('8f754c34-295f-4319-a6b6-aecf6212b8b7', '5dd387cb-85dc-4165-a719-6f48198423e8'),
       ('173c378a-471d-4cac-a989-d5316d88bce3', '5dd387cb-85dc-4165-a719-6f48198423e8'),
       ('88b6958f-0ba8-4e31-add0-8dd43b6ccf0d', '5dd387cb-85dc-4165-a719-6f48198423e8'),
       ('0e3ca8f3-19c7-4c7c-b5e4-c545e968069b', '26db13ab-c76f-4f76-ae57-a9770a46e8ed'),
       ('f77d19da-2eac-460f-bbac-082de4ba54f4', '26db13ab-c76f-4f76-ae57-a9770a46e8ed'),
       ('7a2e15bd-8627-4812-a621-7fffd597c98b', '8f754c34-295f-4319-a6b6-aecf6212b8b7'),
       ('edddc6c3-2c35-4a81-a3c3-7684c75f72fa', '8f754c34-295f-4319-a6b6-aecf6212b8b7'),
       ('2b20e3a7-d5f5-4183-b59e-7a61c4d975c9', '42e28c4c-3ee9-4e2b-8c61-425ee6342463'),
       ('a03a3468-e413-4640-a252-3f4ca337c0b1', '42e28c4c-3ee9-4e2b-8c61-425ee6342463');

-- Без отдела
INSERT INTO users(id, resource_manager_id)
VALUES ('2f4e97a5-e631-4578-b570-0d656f2bbd5b', 'e45bfff0-1b04-4b8a-ac95-9629dff88a3e'),
       ('e1a04fc6-8110-446d-8832-877f4dfe11f4', 'e45bfff0-1b04-4b8a-ac95-9629dff88a3e'),
       ('7ff17452-d42a-4d69-b3d9-ad0288c666c3', 'e45bfff0-1b04-4b8a-ac95-9629dff88a3e'),
       ('9d567cb3-d7b6-4339-afd9-3a8efa2ec121', 'e45bfff0-1b04-4b8a-ac95-9629dff88a3e'),
       ('d1467e23-8d55-460b-b214-0a42bffa48f2', 'e45bfff0-1b04-4b8a-ac95-9629dff88a3e');

-- One to one
INSERT INTO one_to_one_command(id, comment, date_time, is_over, is_deleted, resource_manager_id, user_id)
VALUES ('d3a04c02-db8b-4a99-a9d9-96a6efc99563', 'Обсудить прогресс в изучении английского языка1',
        '2022-08-01 11:11:00.000000', 'false', 'false', '2d6c34cd-8ed1-46cd-a6c2-d360a602c657', '4c353585-be59-41a2-9e36-edc7a0b57b3c'),
       ('720b3a2c-bc22-4e52-8939-fb7d77b2912f', 'Обсудить прогресс в изучении английского языка2',
        '2022-08-02 12:12:00.000000', 'false', 'false', '7368017b-f0d5-4e54-bbe4-72144e4d20e4', '4c353585-be59-41a2-9e36-edc7a0b57b3c'),
       ('f148b72a-6b61-4873-867d-efa937a23551', 'Обсудить прогресс в изучении английского языка3',
        '2022-08-03 13:13:00.000000', 'false', 'false', '7368017b-f0d5-4e54-bbe4-72144e4d20e4', '4c353585-be59-41a2-9e36-edc7a0b57b3c'),
       ('4384465c-169c-4bdb-aadd-76b103079dc2', 'Обсудить прогресс в изучении английского языка4',
        '2022-08-04 14:14:00.000000', 'false', 'false', '2d6c34cd-8ed1-46cd-a6c2-d360a602c657', '4c353585-be59-41a2-9e36-edc7a0b57b3c'),
       ('04628bc8-a272-46d8-9fcd-4fb3e4a0340d', 'Обсудить прогресс в изучении английского языка5',
        '2022-08-05 15:15:00.000000', 'false', 'false', '7368017b-f0d5-4e54-bbe4-72144e4d20e4', '4c353585-be59-41a2-9e36-edc7a0b57b3c'),

       ('857e3120-720e-11ec-90d6-0242ac120003', 'Обсудить переход на новый проект1',
        '2020-08-05 15:15:00.000000', 'false', 'false', '7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'f92e2865-fa9d-4115-a455-c257609a7ffc'),
       ('c085f3f6-689b-11ec-90d6-0242ac120003', 'Обсудить переход на новый проект2',
        '2024-02-05 15:15:00.000000', 'false', 'false', '7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'f92e2865-fa9d-4115-a455-c257609a7ffc'),
       ('e482614a-689b-11ec-90d6-0242ac120003', 'Обсудить переход на новый проект3',
        '2021-03-05 15:15:00.000000', 'true', 'false', '7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'f92e2865-fa9d-4115-a455-c257609a7ffc'),
       ('384f4ebe-689c-11ec-90d6-0242ac120003', 'Обсудить переход на новый проект4',
        '2025-04-05 15:15:00.000000', 'false', 'false', '7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'f92e2865-fa9d-4115-a455-c257609a7ffc'),

       ('3f4bc742-689c-11ec-90d6-0242ac120003', 'Обсудить список литературы1',
        '2020-05-05 15:15:00.000000', 'false', 'false', '7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'ede95340-3219-4d2d-8010-1d1ceaa9ddd0'),
       ('4be62344-689c-11ec-90d6-0242ac120003', 'Обсудить список литературы2',
        '2024-06-05 15:15:00.000000', 'false', 'false', '7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'ede95340-3219-4d2d-8010-1d1ceaa9ddd0'),
       ('5183cd42-689c-11ec-90d6-0242ac120003', 'Обсудить список литературы3',
        '2021-07-05 15:15:00.000000', 'true', 'false', '7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'ede95340-3219-4d2d-8010-1d1ceaa9ddd0'),
       ('d2e14466-7209-11ec-90d6-0242ac120003', 'Обсудить список литературы4',
        '2025-08-05 15:15:00.000000', 'false', 'false', '7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'ede95340-3219-4d2d-8010-1d1ceaa9ddd0');

-- Добавляем имя и фамилию для каждого пользователя
update users set
                 first_name = 'Хед',
                 last_name = 'Менеджменко',
                 email = 'admin@rmtm.work'
where id = 'e45bfff0-1b04-4b8a-ac95-9629dff88a3e';

update users set
                 first_name = 'Босс',
                 last_name = 'Твой',
                 email = 'rd_dev@rmtm.work'
where id = '84be7ac5-e56c-40a2-b21a-655aa74e289c';

update users set
                 first_name = 'Эсэрэм',
                 last_name = 'Девелоперов',
                 email = 'srm1_dev@rmtm.work'
where id = '0922737d-2b19-456b-949b-15740eea99d3';

update users set
                 first_name = 'Элли',
                 last_name = 'Энимешник',
                 email = 'arm2_dev@rmtm.work'
where id = 'db9a9958-79f3-40ab-8fbd-97dd110c4a4e';

update users set
                 first_name = 'Макака',
                 last_name = 'Кодеренко',
                 email = 'employee1_dev@rmtm.work'
where id = '9645183c-8834-434f-be85-336d68fdbee9';

update users set
                 first_name = 'Владислав',
                 last_name = 'Вайтишник',
                 email = 'employee2_dev@rmtm.work'
where id = 'c3f30385-56b9-4457-ab2a-b5d9fb13b2d3';

update users set
                 first_name = 'Геннадий',
                 last_name = 'Джавайло',
                 email = 'employee3_dev@rmtm.work'
where id = '3515fcc1-477d-4b86-a269-8be11125e13b';

update users set
                 first_name = 'Шлёпа',
                 last_name = 'Рыбов',
                 email = 'employee4_dev@rmtm.work'
where id = 'daca41f1-ce5c-4045-a957-e68bef954fbe';

update users set
                 first_name = 'Алексей',
                 last_name = 'Андроидов',
                 email = 'employee5_dev@rmtm.work'
where id = '6a9fae7e-b659-4fe1-b979-323d363b3de9';

update users set
                 first_name = 'Семён',
                 last_name = 'Битард',
                 email = 'employee6_dev@rmtm.work'
where id = '8b690fa4-f5ae-4dc7-a6c4-43332a215075';

update users set
                 first_name = 'Дина',
                 last_name = 'Ковидова',
                 email = 'employee7_dev@rmtm.work'
where id = '80bcad3f-429e-49a8-ad2f-02706cd5f0ba';

update users set
                 first_name = 'Павел',
                 last_name = 'Малинка',
                 email = 'employee8_dev@rmtm.work'
where id = '4c0f167d-73a4-4fa1-8e73-7dca2947d655';

update users set
                 first_name = 'Octopus',
                 last_name = 'Allseeing',
                 email = 'rd_qa@rmtm.work'
where id = '49877fc8-d9d5-479b-ad4f-6ebe5d613dc9';

update users set
                 first_name = 'Supa',
                 last_name = 'Tesuteru',
                 email = 'srm1_qa@rmtm.work'
where id = '2d6c34cd-8ed1-46cd-a6c2-d360a602c657';

update users set
                 first_name = 'Jason',
                 last_name = 'Postman',
                 email = 'arm2_qa@rmtm.work'
where id = '33f0e4e0-0f16-4f7a-83a0-633daa763557';

update users set
                 first_name = 'Idea',
                 last_name = 'Intelli',
                 email = 'employee1_qa@rmtm.work'
where id = '4c353585-be59-41a2-9e36-edc7a0b57b3c';

update users set
                 first_name = 'Soap',
                 last_name = 'Eaten',
                 email = 'employee2_qa@rmtm.work'
where id = 'f92e2865-fa9d-4115-a455-c257609a7ffc';

update users set
                 first_name = 'Micro',
                 last_name = 'Service',
                 email = 'employee3_qa@rmtm.work'
where id = '0967a0bd-a9e7-4d53-a396-5a0d6b00bd4c';

update users set
                 first_name = 'Bug',
                 last_name = 'Introduze',
                 email = 'employee4_qa@rmtm.work'
where id = '3bf804ba-9d2b-4e30-9326-bb3a040425e1';

update users set
                 first_name = 'Rick',
                 last_name = 'Cucumber',
                 email = 'employee5_qa@rmtm.work'
where id = '0221140f-671e-4de7-b485-4a31f9b7b2e4';

update users set
                 first_name = 'Lurker',
                 last_name = 'Chan',
                 email = 'employee6_qa@rmtm.work'
where id = 'ede95340-3219-4d2d-8010-1d1ceaa9ddd0';

update users set
                 first_name = 'Bot',
                 last_name = 'Captcher',
                 email = 'employee7_qa@rmtm.work'
where id = 'afa5a87b-1cab-4844-b7cf-fdcf8cc17fe3';

update users set
                 first_name = 'Weeb',
                 last_name = 'Smart',
                 email = 'employee8_qa@rmtm.work'
where id = '48b0204f-65b5-4940-81a9-64a55d671c2a';

update users set
                 first_name = '早美',
                 last_name = '高橋',
                 email = 'rd_bookkeeping@rmtm.work'
where id = '5b3ec5ae-314f-44fb-b809-8d808fdcfcca';

update users set
                 first_name = '倖世',
                 last_name = '山田',
                 email = 'srm1_bookkeeping@rmtm.work'
where id = '07d756a2-97cf-4ebf-8727-8d68f5e659ff';

update users set
                 first_name = '旭登',
                 last_name = '清水',
                 email = 'rm1_bookkeeping@rmtm.work'
where id = '5dd387cb-85dc-4165-a719-6f48198423e8';

update users set
                 first_name = '春貴',
                 last_name = '石川',
                 email = 'rm2_bookkeeping@rmtm.work'
where id = '42e28c4c-3ee9-4e2b-8c61-425ee6342463';

update users set
                 first_name = '柚咲',
                 last_name = '中島',
                 email = 'arm1_bookkeeping@rmtm.work'
where id = '26db13ab-c76f-4f76-ae57-a9770a46e8ed';

update users set
                 first_name = '小明',
                 last_name = '前田',
                 email = 'arm2_bookkeeping@rmtm.work'
where id = '8f754c34-295f-4319-a6b6-aecf6212b8b7';

update users set
                 first_name = '陽愛',
                 last_name = '小林',
                 email = 'employee1_bookkeeping@rmtm.work'
where id = '173c378a-471d-4cac-a989-d5316d88bce3';

update users set
                 first_name = '優花',
                 last_name = '斎藤',
                 email = 'employee2_bookkeeping@rmtm.work'
where id = '88b6958f-0ba8-4e31-add0-8dd43b6ccf0d';

update users set
                 first_name = '雪',
                 last_name = '佐々木',
                 email = 'employee3_bookkeeping@rmtm.work'
where id = '0e3ca8f3-19c7-4c7c-b5e4-c545e968069b';

update users set
                 first_name = '光',
                 last_name = '井上',
                 email = 'employee4_bookkeeping@rmtm.work'
where id = 'f77d19da-2eac-460f-bbac-082de4ba54f4';

update users set
                 first_name = '雪華',
                 last_name = '山下',
                 email = 'employee5_bookkeeping@rmtm.work'
where id = '7a2e15bd-8627-4812-a621-7fffd597c98b';

update users set
                 first_name = '肖',
                 last_name = '加藤',
                 email = 'employee6_bookkeeping@rmtm.work'
where id = 'edddc6c3-2c35-4a81-a3c3-7684c75f72fa';

update users set
                 first_name = '肖',
                 last_name = '加藤',
                 email = 'employee7_bookkeeping@rmtm.work'
where id = '2b20e3a7-d5f5-4183-b59e-7a61c4d975c9';

update users set
                 first_name = '倖世',
                 last_name = '松本',
                 email = 'employee8_bookkeeping@rmtm.work'
where id = 'a03a3468-e413-4640-a252-3f4ca337c0b1';

update users set
                 first_name = 'Глава',
                 last_name = 'Бездельников',
                 email = 'rd1_bench@rmtm.work'
where id = '2f4e97a5-e631-4578-b570-0d656f2bbd5b';

update users set
                 first_name = 'Лавочка',
                 last_name = 'Сидельникова',
                 email = 'srm1_bench@rmtm.work'
where id = 'e1a04fc6-8110-446d-8832-877f4dfe11f4';

update users set
                 first_name = 'Тихон',
                 last_name = 'Скамейко',
                 email = 'rm1_bench@rmtm.work'
where id = '7ff17452-d42a-4d69-b3d9-ad0288c666c3';

update users set
                 first_name = 'Ждун',
                 last_name = 'Долговремёнов',
                 email = 'arm1_bench@rmtm.work'
where id = '9d567cb3-d7b6-4339-afd9-3a8efa2ec121';

update users set
                 first_name = 'Никита',
                 last_name = 'Нарасслабонев',
                 email = 'employee1_bench@rmtm.work'
where id = 'd1467e23-8d55-460b-b214-0a42bffa48f2';

update users set
                 first_name = 'Виталий',
                 last_name = 'Девопсов',
                 email = 'rm1_dev@rmtm.work'
where id = '7fbdcccb-f166-49af-ac63-a14d87a0d914';

update users set
                 first_name = 'Ангелина',
                 last_name = 'Виртуальмашинова',
                 email = 'rm2_dev@rmtm.work'
where id = '9bdb3b40-5d24-4027-8313-ff633977a401';

update users set
                 first_name = 'Игорь',
                 last_name = 'Факапов',
                 email = 'arm1_dev@rmtm.work'
where id = 'def5cc13-cd01-483c-916d-476ec22cdc68';

update users set
                 first_name = 'Annie',
                 last_name = 'Desu',
                 email = 'rm1_qa@rmtm.work'
where id = '7368017b-f0d5-4e54-bbe4-72144e4d20e4';

update users set
                 first_name = 'Megan',
                 last_name = 'Armdragon',
                 email = 'arm1_qa@rmtm.work'
where id = 'ee9c12c2-ac2b-4432-ac55-d0fdc764af37';

update users set
                 first_name = 'Charles',
                 last_name = 'Testcasey',
                 email = 'rm2_qa@rmtm.work'
where id = 'dde4e690-c634-4991-a751-5af71d3bdf70';

update users set
                 first_name = 'Nobody',
                 last_name = 'None',
                 email = 'unauth@rmtm.work'
where id = 'abd0b6a7-eb63-41dc-9283-d52d9d76fb87';

update users set
                 first_name = 'Isolation',
                 last_name = 'Serializable',
                 email = 'norights@rmtm.work'
where id = '0b7c038c-3154-47c2-b641-5f6d5cf9fe8c';