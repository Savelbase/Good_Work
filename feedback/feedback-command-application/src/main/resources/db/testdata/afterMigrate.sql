delete from event where id is not null;
delete from users where id is not null;
delete from users where id is not null;

INSERT INTO department(id, name, deletable)
VALUES ('a185b51f-2fd4-4f76-a765-de199d32b000', 'Administration', false),
       ('3a9b4a8a-9e8c-4b72-b3cd-4e72090c259b', 'No department', false),
       ('62f69579-ad7f-4dfd-9fea-b718d7c0d968', 'Dev', true),
       ('a1fa6daf-f31f-4fa3-87f2-797467743f0c', 'QA', true),
       ('a3580948-12b7-4a79-936c-69cece8584d3', 'Bookkeeping', true);

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

-- Отдел Administrations
INSERT INTO users(id, email, first_name, last_name, resource_manager_id, department_id)
VALUES ('e45bfff0-1b04-4b8a-ac95-9629dff88a3e', 'admin@rmtm.work', 'Хед', 'Менеджменко', null,
        'a185b51f-2fd4-4f76-a765-de199d32b000');

-- Отдел Development
INSERT INTO users(id, email, first_name, last_name, resource_manager_id, department_id)
VALUES ('84be7ac5-e56c-40a2-b21a-655aa74e289c', 'rd_dev@rmtm.work', 'Босс', 'Твой',
        'e45bfff0-1b04-4b8a-ac95-9629dff88a3e', '62f69579-ad7f-4dfd-9fea-b718d7c0d968'),
       ('7fbdcccb-f166-49af-ac63-a14d87a0d914', 'rm1_dev@rmtm.work', 'Виталий', 'Девопсов',
        '0922737d-2b19-456b-949b-15740eea99d3', '62f69579-ad7f-4dfd-9fea-b718d7c0d968'),
       ('c5f2ae81-4164-4a77-93d0-dc3871e3b247', 'js_ftw@rmtm.work', 'Елизавета', 'Джаваскриптова',
        '84be7ac5-e56c-40a2-b21a-655aa74e289c', '62f69579-ad7f-4dfd-9fea-b718d7c0d968');

-- One to one
INSERT INTO one_to_one(id, date_time, is_over, is_deleted, resource_manager_id, user_id, version)
VALUES ('d3a04c02-db8b-4a99-a9d9-96a6efc99563', '2022-04-05 15:11:00.000000', 'true', 'false',
        '84be7ac5-e56c-40a2-b21a-655aa74e289c', 'c5f2ae81-4164-4a77-93d0-dc3871e3b247', 1);
