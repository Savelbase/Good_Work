TRUNCATE TABLE event, users, activity, city, country, department, role CASCADE;


-- Роль без права авторизации, нужна для тестов
INSERT INTO role(id, name, priority, authorities)
VALUES ('5f341542-5958-433e-b463-7a2f216b749c', 'No auth', 1, ARRAY []::text[]);


INSERT INTO activity (id, name)
VALUES ('99e2f6b1-d90b-4d23-a03c-63d878298757', 'Оценка проктов'),
       ('18e80b50-9904-4d4a-9f39-cdeb6b9475a8', 'Подготовка к проекту'),
       ('6f623ef2-3f83-466a-93ca-721916841e8f', 'Техническое интервью'),
       ('999cb2a0-18b2-4f42-befd-17f6bbb222eb', 'Обучение стажёров'),
       ('672f2c9b-7a19-48cf-807b-91d8ec5f610e', 'Тех. помощь на проекте'),
       ('5ca32404-7fe6-4180-a4a3-d9315f5917bb', 'Выступления на митапах'),
       ('5070294d-0891-495f-993e-1840719f3706', 'Написание статей');


INSERT INTO country (id, name)
VALUES ('079c234e-5c41-41c6-bdf1-87fb58b83a74', 'Россия'),
       ('5636207d-8f7d-4154-9d21-59709b353975', 'United States of America'),
       ('8f56297a-e335-4330-adff-b755975c9ca0', '日本');


INSERT INTO city (id, name, country_id)
VALUES ('cf8a4b92-8c2c-40bb-8403-cfa0964e7c47', 'Москва', '079c234e-5c41-41c6-bdf1-87fb58b83a74'),
       ('f673f7bd-4944-4ad0-ba55-8648ae963e56', 'Санкт-Петербург', '079c234e-5c41-41c6-bdf1-87fb58b83a74'),
       ('a6c289af-4449-4ef7-af7a-6cb1f38076d0', 'Новосибирск', '079c234e-5c41-41c6-bdf1-87fb58b83a74'),
       ('9c407934-7ad7-4ab3-88a3-78cc0df44aeb', 'Екатеринбург', '079c234e-5c41-41c6-bdf1-87fb58b83a74'),
       ('1fcfb908-49fe-4e0f-8207-41854fbda894', 'Нижний Новгород', '079c234e-5c41-41c6-bdf1-87fb58b83a74'),
       ('80da63b2-caa5-470f-92fc-1866a4aae63a', 'New York', '5636207d-8f7d-4154-9d21-59709b353975'),
       ('7fb81c9f-3528-4fe9-9181-df643cf00434', 'Los Angeles', '5636207d-8f7d-4154-9d21-59709b353975'),
       ('082724f3-1472-4709-81ff-28607a905c2c', 'Chicago', '5636207d-8f7d-4154-9d21-59709b353975'),
       ('f543f30a-f5f2-49ff-b2ee-e64bc0931abf', 'Houston', '5636207d-8f7d-4154-9d21-59709b353975'),
       ('fda703c4-ef34-4e7c-9999-cad4db85aaca', 'Phoenix', '5636207d-8f7d-4154-9d21-59709b353975'),
       ('60f7606f-8cb2-435f-aede-13c4d8859f90', '東京', '8f56297a-e335-4330-adff-b755975c9ca0'),
       ('20aec654-212a-4e49-9bab-2a18a8856bbd', '横浜', '8f56297a-e335-4330-adff-b755975c9ca0'),
       ('f9234f17-9f4d-480b-8e40-651a8f8715aa', '大阪市', '8f56297a-e335-4330-adff-b755975c9ca0'),
       ('f3d6ac6d-6760-400e-9bf6-799f494433e6', '名古屋市', '8f56297a-e335-4330-adff-b755975c9ca0'),
       ('52d60e8f-797c-46c4-b35b-dba4e4354f5a', '札幌市', '8f56297a-e335-4330-adff-b755975c9ca0');


INSERT INTO department(id, name, deletable)
VALUES ('a185b51f-2fd4-4f76-a765-de199d32b000', 'Administration', false),
       ('3a9b4a8a-9e8c-4b72-b3cd-4e72090c259b', 'No department', false),
       ('62f69579-ad7f-4dfd-9fea-b718d7c0d968', 'Dev', true),
       ('a1fa6daf-f31f-4fa3-87f2-797467743f0c', 'QA', true),
       ('a3580948-12b7-4a79-936c-69cece8584d3', 'Bookkeeping', true);


INSERT INTO role(id, name, priority, authorities, immutable)
VALUES ('579fc993-6123-419a-ae3c-96b0b230f834', 'Employee', 1, ARRAY [
    'AUTHORIZATION',
    'VIEW_ROLES',
    'VIEW_DEPARTMENTS',
    'EMPLOYEE_LIST',
    'VIEW_INTERVALS',
    'EDIT_COMMENTS'
    ], true);

INSERT INTO role(id, name, priority, authorities, immutable)
VALUES ('9d859c1c-b5f8-4db6-99cd-2784368d4178', 'ARM', 3, ARRAY
    [
    'AUTHORIZATION',
    'VIEW_ROLES',
    'VIEW_DEPARTMENTS',
    'EMPLOYEE_LIST',
    'ONE_TO_ONE',
    'EDIT_FEEDBACKS',
    'ASSESSMENT_GOALS',
    'EDIT_COMMENTS',
    'VIEW_INTERVALS'
    ], true);

INSERT INTO role(id, name, priority, authorities, immutable)
VALUES ('0fea835f-dfbe-400a-bb46-b0a815d2493b', 'RM', 5, ARRAY [
    'AUTHORIZATION',
    'VIEW_ROLES',
    'VIEW_DEPARTMENTS',
    'EMPLOYEE_LIST',
    'ONE_TO_ONE',
    'EDIT_FEEDBACKS',
    'ASSESSMENT_GOALS',
    'EDIT_COMMENTS',
    'EMPLOYEE_CARD',
    'VIEW_INTERVALS'
    ], true);

INSERT INTO role(id, name, priority, authorities, immutable)
VALUES ('a7626246-bc25-4671-9f21-710a1e83f914', 'SRM', 7, ARRAY [
    'AUTHORIZATION',
    'VIEW_ROLES',
    'VIEW_DEPARTMENTS',
    'EMPLOYEE_LIST',
    'ONE_TO_ONE',
    'EDIT_FEEDBACKS',
    'ASSESSMENT_GOALS',
    'EDIT_COMMENTS',
    'EMPLOYEE_CARD',
    'VIEW_INTERVALS'
    ], true);

INSERT INTO role(id, name, priority, authorities, immutable)
VALUES ('43b768e7-c117-4da6-9f41-9b6497aa7b31', 'RD', 9, ARRAY [
    'AUTHORIZATION',
    'VIEW_ROLES',
    'VIEW_DEPARTMENTS',
    'EMPLOYEE_LIST',
    'ONE_TO_ONE',
    'EDIT_FEEDBACKS',
    'ASSESSMENT_GOALS',
    'EDIT_COMMENTS',
    'EMPLOYEE_CARD',
    'USER_STATUS_SETTINGS',
    'ADD_EMPLOYEE_TO_DEPARTMENT',
    'VIEW_INTERVALS'
    ], true);

INSERT INTO role(id, name, priority, authorities, immutable)
VALUES ('96a9b5e3-f523-499f-87ad-ed9f7c17be4a', 'Admin', 10, ARRAY [
    'AUTHORIZATION',
    'EMPLOYEE_LIST',
    'EMPLOYEE_CARD',
    'USER_STATUS_SETTINGS',
    'ADD_EMPLOYEE_TO_DEPARTMENT',
    'VIEW_ONE_TO_ONE',
    'VIEW_FEEDBACKS',
    'VIEW_ASSESSMENT_GOALS',
    'VIEW_COMMENTS',
    'VIEW_ROLES',
    'VIEW_DEPARTMENTS',
    'EDIT_DEPARTMENTS',
    'EDIT_ROLES',
    'EDIT_INTERVALS'
    ], true);

-- Отдел Administrations
INSERT INTO users(id, email, first_name, last_name, resource_manager_id, department_id, role_id)
VALUES ('e45bfff0-1b04-4b8a-ac95-9629dff88a3e', 'admin@rmtm.work', 'Хед', 'Менеджменко', null,
        'a185b51f-2fd4-4f76-a765-de199d32b000', '96a9b5e3-f523-499f-87ad-ed9f7c17be4a');

-- Отдел Development
INSERT INTO users(id, email, first_name, last_name, resource_manager_id, department_id, role_id)
VALUES ('84be7ac5-e56c-40a2-b21a-655aa74e289c', 'rd_dev@rmtm.work', 'Босс', 'Твой',
        'e45bfff0-1b04-4b8a-ac95-9629dff88a3e', '62f69579-ad7f-4dfd-9fea-b718d7c0d968',
        '43b768e7-c117-4da6-9f41-9b6497aa7b31'),
       ('0922737d-2b19-456b-949b-15740eea99d3', 'srm1_dev@rmtm.work', 'Эсэрэм', 'Девелоперов',
        '84be7ac5-e56c-40a2-b21a-655aa74e289c', '62f69579-ad7f-4dfd-9fea-b718d7c0d968',
        'a7626246-bc25-4671-9f21-710a1e83f914'),
       ('7fbdcccb-f166-49af-ac63-a14d87a0d914', 'rm1_dev@rmtm.work', 'Виталий', 'Девопсов',
        '0922737d-2b19-456b-949b-15740eea99d3', '62f69579-ad7f-4dfd-9fea-b718d7c0d968',
        '0fea835f-dfbe-400a-bb46-b0a815d2493b'),
       ('9bdb3b40-5d24-4027-8313-ff633977a401', 'rm2_dev@rmtm.work', 'Ангелина', 'Виртуальмашинова',
        '0922737d-2b19-456b-949b-15740eea99d3', '62f69579-ad7f-4dfd-9fea-b718d7c0d968',
        '0fea835f-dfbe-400a-bb46-b0a815d2493b'),
       ('def5cc13-cd01-483c-916d-476ec22cdc68', 'arm1_dev@rmtm.work', 'Игорь', 'Факапов',
        '7fbdcccb-f166-49af-ac63-a14d87a0d914', '62f69579-ad7f-4dfd-9fea-b718d7c0d968',
        '9d859c1c-b5f8-4db6-99cd-2784368d4178'),
       ('db9a9958-79f3-40ab-8fbd-97dd110c4a4e', 'arm2_dev@rmtm.work', 'Элли', 'Энимешник',
        '7fbdcccb-f166-49af-ac63-a14d87a0d914', '62f69579-ad7f-4dfd-9fea-b718d7c0d968',
        '9d859c1c-b5f8-4db6-99cd-2784368d4178'),
       ('9645183c-8834-434f-be85-336d68fdbee9', 'employee1_dev@rmtm.work', 'Макака', 'Кодеренко',
        '7fbdcccb-f166-49af-ac63-a14d87a0d914', '62f69579-ad7f-4dfd-9fea-b718d7c0d968',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('c3f30385-56b9-4457-ab2a-b5d9fb13b2d3', 'employee2_dev@rmtm.work', 'Владислав', 'Вайтишник',
        '7fbdcccb-f166-49af-ac63-a14d87a0d914', '62f69579-ad7f-4dfd-9fea-b718d7c0d968',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('3515fcc1-477d-4b86-a269-8be11125e13b', 'employee3_dev@rmtm.work', 'Геннадий', 'Джавайло',
        'def5cc13-cd01-483c-916d-476ec22cdc68', '62f69579-ad7f-4dfd-9fea-b718d7c0d968',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('daca41f1-ce5c-4045-a957-e68bef954fbe', 'employee4_dev@rmtm.work', 'Шлёпа', 'Рыбов',
        'def5cc13-cd01-483c-916d-476ec22cdc68', '62f69579-ad7f-4dfd-9fea-b718d7c0d968',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('6a9fae7e-b659-4fe1-b979-323d363b3de9', 'employee5_dev@rmtm.work', 'Алексей', 'Андроидов',
        'db9a9958-79f3-40ab-8fbd-97dd110c4a4e', '62f69579-ad7f-4dfd-9fea-b718d7c0d968',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('8b690fa4-f5ae-4dc7-a6c4-43332a215075', 'employee6_dev@rmtm.work', 'Семён', 'Битард',
        'db9a9958-79f3-40ab-8fbd-97dd110c4a4e', '62f69579-ad7f-4dfd-9fea-b718d7c0d968',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('80bcad3f-429e-49a8-ad2f-02706cd5f0ba', 'employee7_dev@rmtm.work', 'Дина', 'Ковидова',
        '9bdb3b40-5d24-4027-8313-ff633977a401', '62f69579-ad7f-4dfd-9fea-b718d7c0d968',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('4c0f167d-73a4-4fa1-8e73-7dca2947d655', 'employee8_dev@rmtm.work', 'Павел', 'Малинка',
        '9bdb3b40-5d24-4027-8313-ff633977a401', '62f69579-ad7f-4dfd-9fea-b718d7c0d968',
        '579fc993-6123-419a-ae3c-96b0b230f834');

-- Отдел QA
INSERT INTO users(id, email, first_name, last_name, resource_manager_id, department_id, role_id)
VALUES ('49877fc8-d9d5-479b-ad4f-6ebe5d613dc9', 'rd_qa@rmtm.work', 'Octopus', 'Allseeing',
        'e45bfff0-1b04-4b8a-ac95-9629dff88a3e', 'a1fa6daf-f31f-4fa3-87f2-797467743f0c',
        '43b768e7-c117-4da6-9f41-9b6497aa7b31'),
       ('2d6c34cd-8ed1-46cd-a6c2-d360a602c657', 'srm1_qa@rmtm.work', 'Supa', 'Tesuteru',
        '49877fc8-d9d5-479b-ad4f-6ebe5d613dc9', 'a1fa6daf-f31f-4fa3-87f2-797467743f0c',
        'a7626246-bc25-4671-9f21-710a1e83f914'),
       ('7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'rm1_qa@rmtm.work', 'Annie', 'Desu',
        '2d6c34cd-8ed1-46cd-a6c2-d360a602c657', 'a1fa6daf-f31f-4fa3-87f2-797467743f0c',
        '0fea835f-dfbe-400a-bb46-b0a815d2493b'),
       ('dde4e690-c634-4991-a751-5af71d3bdf70', 'rm2_qa@rmtm.work', 'Charles', 'Testcasey',
        '2d6c34cd-8ed1-46cd-a6c2-d360a602c657', 'a1fa6daf-f31f-4fa3-87f2-797467743f0c',
        '0fea835f-dfbe-400a-bb46-b0a815d2493b'),
       ('ee9c12c2-ac2b-4432-ac55-d0fdc764af37', 'arm1_qa@rmtm.work', 'Megan', 'Armdragon',
        '7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'a1fa6daf-f31f-4fa3-87f2-797467743f0c',
        '9d859c1c-b5f8-4db6-99cd-2784368d4178'),
       ('33f0e4e0-0f16-4f7a-83a0-633daa763557', 'arm2_qa@rmtm.work', 'Jason', 'Postman',
        '7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'a1fa6daf-f31f-4fa3-87f2-797467743f0c',
        '9d859c1c-b5f8-4db6-99cd-2784368d4178'),
       ('4c353585-be59-41a2-9e36-edc7a0b57b3c', 'employee1_qa@rmtm.work', 'Idea', 'Intelli',
        '7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'a1fa6daf-f31f-4fa3-87f2-797467743f0c',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('f92e2865-fa9d-4115-a455-c257609a7ffc', 'employee2_qa@rmtm.work', 'Soap', 'Eaten',
        '7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'a1fa6daf-f31f-4fa3-87f2-797467743f0c',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('0967a0bd-a9e7-4d53-a396-5a0d6b00bd4c', 'employee3_qa@rmtm.work', 'Micro', 'Service',
        'ee9c12c2-ac2b-4432-ac55-d0fdc764af37', 'a1fa6daf-f31f-4fa3-87f2-797467743f0c',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('3bf804ba-9d2b-4e30-9326-bb3a040425e1', 'employee4_qa@rmtm.work', 'Bug', 'Introduze',
        'ee9c12c2-ac2b-4432-ac55-d0fdc764af37', 'a1fa6daf-f31f-4fa3-87f2-797467743f0c',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('0221140f-671e-4de7-b485-4a31f9b7b2e4', 'employee5_qa@rmtm.work', 'Rick', 'Cucumber',
        '33f0e4e0-0f16-4f7a-83a0-633daa763557', 'a1fa6daf-f31f-4fa3-87f2-797467743f0c',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('ede95340-3219-4d2d-8010-1d1ceaa9ddd0', 'employee6_qa@rmtm.work', 'Lurker', 'Chan',
        '33f0e4e0-0f16-4f7a-83a0-633daa763557', 'a1fa6daf-f31f-4fa3-87f2-797467743f0c',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('afa5a87b-1cab-4844-b7cf-fdcf8cc17fe3', 'employee7_qa@rmtm.work', 'Bot', 'Captcher',
        'dde4e690-c634-4991-a751-5af71d3bdf70', 'a1fa6daf-f31f-4fa3-87f2-797467743f0c',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('48b0204f-65b5-4940-81a9-64a55d671c2a', 'employee8_qa@rmtm.work', 'Weeb', 'Smart',
        'dde4e690-c634-4991-a751-5af71d3bdf70', 'a1fa6daf-f31f-4fa3-87f2-797467743f0c',
        '579fc993-6123-419a-ae3c-96b0b230f834');

-- Отдел Bookkeeping
INSERT INTO users(id, email, first_name, last_name, resource_manager_id, department_id, role_id)
VALUES ('5b3ec5ae-314f-44fb-b809-8d808fdcfcca', 'rd_bookkeeping@rmtm.work', '早美', '高橋',
        'e45bfff0-1b04-4b8a-ac95-9629dff88a3e', 'a3580948-12b7-4a79-936c-69cece8584d3',
        '43b768e7-c117-4da6-9f41-9b6497aa7b31'),
       ('07d756a2-97cf-4ebf-8727-8d68f5e659ff', 'srm1_bookkeeping@rmtm.work', '倖世', '山田',
        '5b3ec5ae-314f-44fb-b809-8d808fdcfcca', 'a3580948-12b7-4a79-936c-69cece8584d3',
        'a7626246-bc25-4671-9f21-710a1e83f914'),
       ('5dd387cb-85dc-4165-a719-6f48198423e8', 'rm1_bookkeeping@rmtm.work', '旭登', '清水',
        '07d756a2-97cf-4ebf-8727-8d68f5e659ff', 'a3580948-12b7-4a79-936c-69cece8584d3',
        '0fea835f-dfbe-400a-bb46-b0a815d2493b'),
       ('42e28c4c-3ee9-4e2b-8c61-425ee6342463', 'rm2_bookkeeping@rmtm.work', '春貴', '石川',
        '07d756a2-97cf-4ebf-8727-8d68f5e659ff',
        'a3580948-12b7-4a79-936c-69cece8584d3', '0fea835f-dfbe-400a-bb46-b0a815d2493b'),
       ('26db13ab-c76f-4f76-ae57-a9770a46e8ed', 'arm1_bookkeeping@rmtm.work', '柚咲', '中島',
        '5dd387cb-85dc-4165-a719-6f48198423e8',
        'a3580948-12b7-4a79-936c-69cece8584d3', '9d859c1c-b5f8-4db6-99cd-2784368d4178'),
       ('8f754c34-295f-4319-a6b6-aecf6212b8b7', 'arm2_bookkeeping@rmtm.work', '小明', '前田',
        '5dd387cb-85dc-4165-a719-6f48198423e8',
        'a3580948-12b7-4a79-936c-69cece8584d3', '9d859c1c-b5f8-4db6-99cd-2784368d4178'),
       ('173c378a-471d-4cac-a989-d5316d88bce3', 'employee1_bookkeeping@rmtm.work', '陽愛', '小林',
        '5dd387cb-85dc-4165-a719-6f48198423e8', 'a3580948-12b7-4a79-936c-69cece8584d3',
        '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('88b6958f-0ba8-4e31-add0-8dd43b6ccf0d', 'employee2_bookkeeping@rmtm.work', '優花', '斎藤',
        '5dd387cb-85dc-4165-a719-6f48198423e8',
        'a3580948-12b7-4a79-936c-69cece8584d3', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('0e3ca8f3-19c7-4c7c-b5e4-c545e968069b', 'employee3_bookkeeping@rmtm.work', '雪', '佐々木',
        '26db13ab-c76f-4f76-ae57-a9770a46e8ed',
        'a3580948-12b7-4a79-936c-69cece8584d3', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('f77d19da-2eac-460f-bbac-082de4ba54f4', 'employee4_bookkeeping@rmtm.work', '光', '井上',
        '26db13ab-c76f-4f76-ae57-a9770a46e8ed',
        'a3580948-12b7-4a79-936c-69cece8584d3', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('7a2e15bd-8627-4812-a621-7fffd597c98b', 'employee5_bookkeeping@rmtm.work', '雪華', '山下',
        '8f754c34-295f-4319-a6b6-aecf6212b8b7',
        'a3580948-12b7-4a79-936c-69cece8584d3', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('edddc6c3-2c35-4a81-a3c3-7684c75f72fa', 'employee6_bookkeeping@rmtm.work', '肖', '加藤',
        '8f754c34-295f-4319-a6b6-aecf6212b8b7',
        'a3580948-12b7-4a79-936c-69cece8584d3', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('2b20e3a7-d5f5-4183-b59e-7a61c4d975c9', 'employee7_bookkeeping@rmtm.work', '肖', '加藤',
        '42e28c4c-3ee9-4e2b-8c61-425ee6342463',
        'a3580948-12b7-4a79-936c-69cece8584d3', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('a03a3468-e413-4640-a252-3f4ca337c0b1', 'employee8_bookkeeping@rmtm.work', '倖世', '松本',
        '42e28c4c-3ee9-4e2b-8c61-425ee6342463',
        'a3580948-12b7-4a79-936c-69cece8584d3', '579fc993-6123-419a-ae3c-96b0b230f834');

-- Без отдела
INSERT INTO users(id, email, first_name, last_name, resource_manager_id, department_id, role_id)
VALUES ('2f4e97a5-e631-4578-b570-0d656f2bbd5b', 'rd1_bench@rmtm.work', 'Глава', 'Бездельников',
        'e45bfff0-1b04-4b8a-ac95-9629dff88a3e', '3a9b4a8a-9e8c-4b72-b3cd-4e72090c259b',
        '43b768e7-c117-4da6-9f41-9b6497aa7b31'),
       ('e1a04fc6-8110-446d-8832-877f4dfe11f4', 'srm1_bench@rmtm.work', 'Лавочка', 'Сидельникова',
        'e45bfff0-1b04-4b8a-ac95-9629dff88a3e', '3a9b4a8a-9e8c-4b72-b3cd-4e72090c259b',
        'a7626246-bc25-4671-9f21-710a1e83f914'),
       ('7ff17452-d42a-4d69-b3d9-ad0288c666c3', 'rm1_bench@rmtm.work', 'Тихон', 'Скамейко',
        'e45bfff0-1b04-4b8a-ac95-9629dff88a3e', '3a9b4a8a-9e8c-4b72-b3cd-4e72090c259b',
        '0fea835f-dfbe-400a-bb46-b0a815d2493b'),
       ('9d567cb3-d7b6-4339-afd9-3a8efa2ec121', 'arm1_bench@rmtm.work', 'Ждун', 'Долговремёнов',
        'e45bfff0-1b04-4b8a-ac95-9629dff88a3e', '3a9b4a8a-9e8c-4b72-b3cd-4e72090c259b',
        '9d859c1c-b5f8-4db6-99cd-2784368d4178'),
       ('d1467e23-8d55-460b-b214-0a42bffa48f2', 'employee1_bench@rmtm.work', 'Никита', 'Нарасслабонев',
        'e45bfff0-1b04-4b8a-ac95-9629dff88a3e', '3a9b4a8a-9e8c-4b72-b3cd-4e72090c259b',
        '579fc993-6123-419a-ae3c-96b0b230f834');


UPDATE department
SET head_id='e45bfff0-1b04-4b8a-ac95-9629dff88a3e'
WHERE id = 'a185b51f-2fd4-4f76-a765-de199d32b000';
UPDATE department
SET head_id='e45bfff0-1b04-4b8a-ac95-9629dff88a3e'
WHERE id = '3a9b4a8a-9e8c-4b72-b3cd-4e72090c259b';
UPDATE department
SET head_id='84be7ac5-e56c-40a2-b21a-655aa74e289c'
WHERE id = '62f69579-ad7f-4dfd-9fea-b718d7c0d968';
UPDATE department
SET head_id='49877fc8-d9d5-479b-ad4f-6ebe5d613dc9'
WHERE id = 'a1fa6daf-f31f-4fa3-87f2-797467743f0c';
UPDATE department
SET head_id='5b3ec5ae-314f-44fb-b809-8d808fdcfcca'
WHERE id = 'a3580948-12b7-4a79-936c-69cece8584d3';

-- For QA testing
INSERT INTO role(id, name, priority, authorities)
VALUES ('6f757d76-b530-41b8-982d-5e09b8b1f124', 'No rights', 1, ARRAY [
    'AUTHORIZATION']);

INSERT INTO users(id, email, first_name, last_name, resource_manager_id, department_id, role_id)
VALUES ('abd0b6a7-eb63-41dc-9283-d52d9d76fb87', 'unauth@rmtm.work', 'Nobody', 'None',
        'e45bfff0-1b04-4b8a-ac95-9629dff88a3e', '3a9b4a8a-9e8c-4b72-b3cd-4e72090c259b',
        '5f341542-5958-433e-b463-7a2f216b749c'),
       ('0b7c038c-3154-47c2-b641-5f6d5cf9fe8c', 'norights@rmtm.work', 'Isolation', 'Serializable',
        'e45bfff0-1b04-4b8a-ac95-9629dff88a3e', '3a9b4a8a-9e8c-4b72-b3cd-4e72090c259b',
        '6f757d76-b530-41b8-982d-5e09b8b1f124');