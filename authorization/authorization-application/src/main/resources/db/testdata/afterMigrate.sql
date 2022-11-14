TRUNCATE TABLE event, refresh_token, users, role, whitelist_email CASCADE;

-- Роль без права авторизации, нужна для тестов
INSERT INTO role(id, priority, authorities)
VALUES ('5f341542-5958-433e-b463-7a2f216b749c', 1, ARRAY []::text[]);


INSERT INTO role(id, priority, authorities)
VALUES ('579fc993-6123-419a-ae3c-96b0b230f834', 1, ARRAY [
    'AUTHORIZATION',
    'VIEW_ROLES',
    'VIEW_DEPARTMENTS',
    'EMPLOYEE_LIST',
    'VIEW_INTERVALS',
    'EDIT_COMMENTS'
    ]);

INSERT INTO role(id, priority, authorities)
VALUES ('9d859c1c-b5f8-4db6-99cd-2784368d4178', 3, ARRAY
    [
    'AUTHORIZATION',
    'VIEW_ROLES',
    'VIEW_DEPARTMENTS',
    'EMPLOYEE_LIST',
    'ONE_TO_ONE',
    'VIEW_ONE_TO_ONE',
    'EDIT_FEEDBACKS',
    'ASSESSMENT_GOALS',
    'EDIT_COMMENTS',
    'VIEW_INTERVALS'
    ]);

INSERT INTO role(id, priority, authorities)
VALUES ('0fea835f-dfbe-400a-bb46-b0a815d2493b', 5, ARRAY [
    'AUTHORIZATION',
    'VIEW_ROLES',
    'VIEW_DEPARTMENTS',
    'EMPLOYEE_LIST',
    'ONE_TO_ONE',
    'VIEW_ONE_TO_ONE',
    'EDIT_FEEDBACKS',
    'ASSESSMENT_GOALS',
    'EDIT_COMMENTS',
    'EMPLOYEE_CARD',
    'VIEW_INTERVALS'
    ]);

INSERT INTO role(id, priority, authorities)
VALUES ('a7626246-bc25-4671-9f21-710a1e83f914', 7, ARRAY [
    'AUTHORIZATION',
    'VIEW_ROLES',
    'VIEW_DEPARTMENTS',
    'EMPLOYEE_LIST',
    'ONE_TO_ONE',
    'VIEW_ONE_TO_ONE',
    'EDIT_FEEDBACKS',
    'ASSESSMENT_GOALS',
    'EDIT_COMMENTS',
    'EMPLOYEE_CARD',
    'VIEW_INTERVALS'
    ]);

INSERT INTO role(id, priority, authorities)
VALUES ('43b768e7-c117-4da6-9f41-9b6497aa7b31', 9, ARRAY [
    'AUTHORIZATION',
    'VIEW_ROLES',
    'VIEW_DEPARTMENTS',
    'EMPLOYEE_LIST',
    'ONE_TO_ONE',
    'VIEW_ONE_TO_ONE',
    'EDIT_FEEDBACKS',
    'ASSESSMENT_GOALS',
    'EDIT_COMMENTS',
    'EMPLOYEE_CARD',
    'USER_STATUS_SETTINGS',
    'ADD_EMPLOYEE_TO_DEPARTMENT',
    'VIEW_INTERVALS'
    ]);

INSERT INTO role(id, priority, authorities)
VALUES ('96a9b5e3-f523-499f-87ad-ed9f7c17be4a', 10, ARRAY [
    'AUTHORIZATION',
    'EMPLOYEE_LIST',
    'EMPLOYEE_CARD',
    'USER_STATUS_SETTINGS',
    'ADD_EMPLOYEE_TO_DEPARTMENT',
    'VIEW_ONE_TO_ONE',
    'VIEW_FEEDBACKS',
    'VIEW_ASSESSMENT_GOALS',
    'VIEW_COMMENTS',
    'EDIT_COMMENTS',
    'VIEW_ROLES',
    'VIEW_DEPARTMENTS',
    'EDIT_DEPARTMENTS',
    'EDIT_ROLES',
    'EDIT_INTERVALS'
    ]);


-- Отдел Administration
INSERT INTO users(id, email, password, role_id)
VALUES ('e45bfff0-1b04-4b8a-ac95-9629dff88a3e', 'admin@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '96a9b5e3-f523-499f-87ad-ed9f7c17be4a');

-- Отдел Development
INSERT INTO users(id, email, password, role_id)
VALUES ('84be7ac5-e56c-40a2-b21a-655aa74e289c', 'rd_dev@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '43b768e7-c117-4da6-9f41-9b6497aa7b31'),
       ('0922737d-2b19-456b-949b-15740eea99d3', 'srm1_dev@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', 'a7626246-bc25-4671-9f21-710a1e83f914'),
       ('7fbdcccb-f166-49af-ac63-a14d87a0d914', 'rm1_dev@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '0fea835f-dfbe-400a-bb46-b0a815d2493b'),
       ('9bdb3b40-5d24-4027-8313-ff633977a401', 'rm2_dev@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '0fea835f-dfbe-400a-bb46-b0a815d2493b'),
       ('def5cc13-cd01-483c-916d-476ec22cdc68', 'arm1_dev@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '9d859c1c-b5f8-4db6-99cd-2784368d4178'),
       ('db9a9958-79f3-40ab-8fbd-97dd110c4a4e', 'arm2_dev@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '9d859c1c-b5f8-4db6-99cd-2784368d4178'),
       ('9645183c-8834-434f-be85-336d68fdbee9', 'employee1_dev@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('c3f30385-56b9-4457-ab2a-b5d9fb13b2d3', 'employee2_dev@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('3515fcc1-477d-4b86-a269-8be11125e13b', 'employee3_dev@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('daca41f1-ce5c-4045-a957-e68bef954fbe', 'employee4_dev@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('6a9fae7e-b659-4fe1-b979-323d363b3de9', 'employee5_dev@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('8b690fa4-f5ae-4dc7-a6c4-43332a215075', 'employee6_dev@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('80bcad3f-429e-49a8-ad2f-02706cd5f0ba', 'employee7_dev@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('4c0f167d-73a4-4fa1-8e73-7dca2947d655', 'employee8_dev@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834');

-- Отдел QA
INSERT INTO users(id, email, password, role_id)
VALUES ('49877fc8-d9d5-479b-ad4f-6ebe5d613dc9', 'rd_qa@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '43b768e7-c117-4da6-9f41-9b6497aa7b31'),
       ('2d6c34cd-8ed1-46cd-a6c2-d360a602c657', 'srm1_qa@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', 'a7626246-bc25-4671-9f21-710a1e83f914'),
       ('7368017b-f0d5-4e54-bbe4-72144e4d20e4', 'rm1_qa@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '0fea835f-dfbe-400a-bb46-b0a815d2493b'),
       ('dde4e690-c634-4991-a751-5af71d3bdf70', 'rm2_qa@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '0fea835f-dfbe-400a-bb46-b0a815d2493b'),
       ('ee9c12c2-ac2b-4432-ac55-d0fdc764af37', 'arm1_qa@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '9d859c1c-b5f8-4db6-99cd-2784368d4178'),
       ('33f0e4e0-0f16-4f7a-83a0-633daa763557', 'arm2_qa@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '9d859c1c-b5f8-4db6-99cd-2784368d4178'),
       ('4c353585-be59-41a2-9e36-edc7a0b57b3c', 'employee1_qa@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('f92e2865-fa9d-4115-a455-c257609a7ffc', 'employee2_qa@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('0967a0bd-a9e7-4d53-a396-5a0d6b00bd4c', 'employee3_qa@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('3bf804ba-9d2b-4e30-9326-bb3a040425e1', 'employee4_qa@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('0221140f-671e-4de7-b485-4a31f9b7b2e4', 'employee5_qa@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('ede95340-3219-4d2d-8010-1d1ceaa9ddd0', 'employee6_qa@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('afa5a87b-1cab-4844-b7cf-fdcf8cc17fe3', 'employee7_qa@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('48b0204f-65b5-4940-81a9-64a55d671c2a', 'employee8_qa@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834');

-- Отдел Bookkeeping
INSERT INTO users(id, email, password, role_id)
VALUES ('5b3ec5ae-314f-44fb-b809-8d808fdcfcca', 'rd_bookkeeping@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '43b768e7-c117-4da6-9f41-9b6497aa7b31'),
       ('07d756a2-97cf-4ebf-8727-8d68f5e659ff', 'srm1_bookkeeping@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', 'a7626246-bc25-4671-9f21-710a1e83f914'),
       ('5dd387cb-85dc-4165-a719-6f48198423e8', 'rm1_bookkeeping@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '0fea835f-dfbe-400a-bb46-b0a815d2493b'),
       ('42e28c4c-3ee9-4e2b-8c61-425ee6342463', 'rm2_bookkeeping@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '0fea835f-dfbe-400a-bb46-b0a815d2493b'),
       ('26db13ab-c76f-4f76-ae57-a9770a46e8ed', 'arm1_bookkeeping@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '9d859c1c-b5f8-4db6-99cd-2784368d4178'),
       ('8f754c34-295f-4319-a6b6-aecf6212b8b7', 'arm2_bookkeeping@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '9d859c1c-b5f8-4db6-99cd-2784368d4178'),
       ('173c378a-471d-4cac-a989-d5316d88bce3', 'employee1_bookkeeping@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('88b6958f-0ba8-4e31-add0-8dd43b6ccf0d', 'employee2_bookkeeping@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('0e3ca8f3-19c7-4c7c-b5e4-c545e968069b', 'employee3_bookkeeping@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('f77d19da-2eac-460f-bbac-082de4ba54f4', 'employee4_bookkeeping@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('7a2e15bd-8627-4812-a621-7fffd597c98b', 'employee5_bookkeeping@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('edddc6c3-2c35-4a81-a3c3-7684c75f72fa', 'employee6_bookkeeping@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('2b20e3a7-d5f5-4183-b59e-7a61c4d975c9', 'employee7_bookkeeping@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834'),
       ('a03a3468-e413-4640-a252-3f4ca337c0b1', 'employee8_bookkeeping@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834');

-- Без отдела
INSERT INTO users(id, email, password, role_id)
VALUES ('2f4e97a5-e631-4578-b570-0d656f2bbd5b', 'rd1_bench@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '43b768e7-c117-4da6-9f41-9b6497aa7b31'),
       ('e1a04fc6-8110-446d-8832-877f4dfe11f4', 'srm1_bench@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', 'a7626246-bc25-4671-9f21-710a1e83f914'),
       ('7ff17452-d42a-4d69-b3d9-ad0288c666c3', 'rm1_bench@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '0fea835f-dfbe-400a-bb46-b0a815d2493b'),
       ('9d567cb3-d7b6-4339-afd9-3a8efa2ec121', 'arm1_bench@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '9d859c1c-b5f8-4db6-99cd-2784368d4178'),
       ('d1467e23-8d55-460b-b214-0a42bffa48f2', 'employee1_bench@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '579fc993-6123-419a-ae3c-96b0b230f834');


INSERT INTO whitelist_email(id, email)
VALUES ('e6f2bfea-0bc3-11ec-9a03-0242ac130003', 'testtestovich1233@gmail.com');

-- For QA testing
INSERT INTO role(id, priority, authorities)
VALUES ('6f757d76-b530-41b8-982d-5e09b8b1f124', 1, ARRAY ['AUTHORIZATION']);

INSERT INTO users(id, email, password, role_id)
VALUES ('abd0b6a7-eb63-41dc-9283-d52d9d76fb87', 'unauth@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '5f341542-5958-433e-b463-7a2f216b749c'),
       ('0b7c038c-3154-47c2-b641-5f6d5cf9fe8c', 'norights@rmtm.work',
        '$2a$10$QukzCpOJch05vi9T5vY4se97pZM7mhVR04Xsza4jtVs.Z8Q18AZWC', '6f757d76-b530-41b8-982d-5e09b8b1f124');