-- Удаляем тестовых юзеров и встречи и накатываем заново
DELETE
FROM one_to_one_query
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

-- Отдел QA
INSERT INTO users(id, first_name, last_name)
VALUES ('49877fc8-d9d5-479b-ad4f-6ebe5d613dc9', 'Octopus', 'Allseeing'),
       ('2d6c34cd-8ed1-46cd-a6c2-d360a602c657', 'Supa', 'Tesuteru'),
       ('7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'Annie', 'Desu'),
       ('dde4e690-c634-4991-a751-5af71d3bdf70', 'Charles', 'Testcasey'),
       ('ee9c12c2-ac2b-4432-ac55-d0fdc764af37', 'Megan', 'Armdragon'),
       ('33f0e4e0-0f16-4f7a-83a0-633daa763557', 'Jason', 'Postman'),
       ('4c353585-be59-41a2-9e36-edc7a0b57b3c', 'Idea', 'Intelli'),
       ('f92e2865-fa9d-4115-a455-c257609a7ffc', 'Soap', 'Eaten'),
       ('0967a0bd-a9e7-4d53-a396-5a0d6b00bd4c', 'Micro', 'Service'),
       ('3bf804ba-9d2b-4e30-9326-bb3a040425e1', 'Bug', 'Introduze'),
       ('0221140f-671e-4de7-b485-4a31f9b7b2e4', 'Rick', 'Cucumber'),
       ('ede95340-3219-4d2d-8010-1d1ceaa9ddd0', 'Lurker', 'Chan'),
       ('afa5a87b-1cab-4844-b7cf-fdcf8cc17fe3', 'Bot', 'Captcher'),
       ('48b0204f-65b5-4940-81a9-64a55d671c2a', 'Weeb', 'Smart');

-- Отдел Administration
INSERT INTO users(id, first_name, last_name)
VALUES ('e45bfff0-1b04-4b8a-ac95-9629dff88a3e', 'Хед', 'Менеджменко');

-- Отдел Development
INSERT INTO users(id, first_name, last_name)
VALUES ('84be7ac5-e56c-40a2-b21a-655aa74e289c', 'Босс', 'Твой'),
       ('0922737d-2b19-456b-949b-15740eea99d3', 'Эсэрэм', 'Девелоперов'),
       ('7fbdcccb-f166-49af-ac63-a14d87a0d914', 'Виталий', 'Девопсов'),
       ('9bdb3b40-5d24-4027-8313-ff633977a401', 'Ангелина', 'Виртуальмашинова'),
       ('def5cc13-cd01-483c-916d-476ec22cdc68', 'Игорь', 'Факапов'),
       ('db9a9958-79f3-40ab-8fbd-97dd110c4a4e', 'Элли', 'Энимешник'),
       ('9645183c-8834-434f-be85-336d68fdbee9', 'Макака', 'Кодеренко'),
       ('c3f30385-56b9-4457-ab2a-b5d9fb13b2d3', 'Владислав', 'Вайтишник'),
       ('3515fcc1-477d-4b86-a269-8be11125e13b', 'Геннадий', 'Джавайло'),
       ('daca41f1-ce5c-4045-a957-e68bef954fbe', 'Шлёпа', 'Рыбов'),
       ('6a9fae7e-b659-4fe1-b979-323d363b3de9', 'Алексей', 'Андроидов'),
       ('8b690fa4-f5ae-4dc7-a6c4-43332a215075', 'Семён', 'Битард'),
       ('80bcad3f-429e-49a8-ad2f-02706cd5f0ba', 'Дина', 'Ковидова'),
       ('4c0f167d-73a4-4fa1-8e73-7dca2947d655', 'Павел', 'Малинка');

-- Отдел Bookkeeping
INSERT INTO users(id, first_name, last_name)
VALUES ('5b3ec5ae-314f-44fb-b809-8d808fdcfcca', '早美', '高橋'),
       ('07d756a2-97cf-4ebf-8727-8d68f5e659ff', '倖世', '山田'),
       ('5dd387cb-85dc-4165-a719-6f48198423e8', '旭登', '清水'),
       ('42e28c4c-3ee9-4e2b-8c61-425ee6342463', '春貴', '石川'),
       ('26db13ab-c76f-4f76-ae57-a9770a46e8ed', '柚咲', '中島'),
       ('8f754c34-295f-4319-a6b6-aecf6212b8b7', '小明', '前田'),
       ('173c378a-471d-4cac-a989-d5316d88bce3', '陽愛', '小林'),
       ('88b6958f-0ba8-4e31-add0-8dd43b6ccf0d', '優花', '斎藤'),
       ('0e3ca8f3-19c7-4c7c-b5e4-c545e968069b', '雪', '佐々木'),
       ('f77d19da-2eac-460f-bbac-082de4ba54f4', '光', '井上'),
       ('7a2e15bd-8627-4812-a621-7fffd597c98b', '雪華', '山下'),
       ('edddc6c3-2c35-4a81-a3c3-7684c75f72fa', '肖', '加藤'),
       ('2b20e3a7-d5f5-4183-b59e-7a61c4d975c9', '肖', '加藤'),
       ('a03a3468-e413-4640-a252-3f4ca337c0b1', '倖世', '松本');

-- Без отдела
INSERT INTO users(id, first_name, last_name)
VALUES ('2f4e97a5-e631-4578-b570-0d656f2bbd5b', 'Глава', 'Бездельников'),
       ('e1a04fc6-8110-446d-8832-877f4dfe11f4', 'Лавочка', 'Сидельникова'),
       ('7ff17452-d42a-4d69-b3d9-ad0288c666c3', 'Тихон', 'Скамейко'),
       ('9d567cb3-d7b6-4339-afd9-3a8efa2ec121', 'Ждун', 'Долговремёнов'),
       ('d1467e23-8d55-460b-b214-0a42bffa48f2', 'Никита', 'Нарасслабонев');


-- One to one
INSERT INTO one_to_one_query(id, comment, date_time, is_over, is_deleted, resource_manager_id, user_id)
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
        '2025-08-05 15:15:00.000000', 'false', 'false', '7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'ede95340-3219-4d2d-8010-1d1ceaa9ddd0')
