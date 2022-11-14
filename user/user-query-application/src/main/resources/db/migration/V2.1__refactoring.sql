TRUNCATE role;

DELETE
FROM authority
WHERE id = '4872209a-f29d-4fe5-9244-27179c7fcb04';
DELETE
FROM authority
WHERE id = '2597e5d8-7bd0-46bc-a17b-f810ecb0d37f';

UPDATE authority SET name='ADD_EMPLOYEE_TO_DEPARTMENT' WHERE id='41a15085-7b43-406a-8e8c-9c2952e93e45';
UPDATE authority SET name='SETTINGS' WHERE id='e84a5f75-ab97-414c-ac0f-7f187cafc29e';

INSERT INTO authority VALUES ('ac541cf7-18cd-4583-916b-42d640d0443f', 'VIEW_ROLES');
INSERT INTO authority VALUES ('cb069b8b-f0ab-484a-9943-a3f829f1919f', 'VIEW_DEPARTMENTS');


ALTER TABLE users
    DROP CONSTRAINT fk_users_department;

ALTER TABLE users
    ADD COLUMN department_name text;
ALTER TABLE users
    ADD COLUMN role_name text;


ALTER TABLE activity
    RENAME COLUMN is_deleted TO deleted;
ALTER TABLE city
    RENAME COLUMN is_deleted TO deleted;
ALTER TABLE country
    RENAME COLUMN is_deleted TO deleted;
ALTER TABLE department
    RENAME COLUMN is_deleted TO deleted;
ALTER TABLE role
    RENAME COLUMN is_deleted TO deleted;


ALTER TABLE activity
    RENAME CONSTRAINT uk_activity_name TO cx_activity_name;
CLUSTER activity USING cx_activity_name;

ALTER TABLE authority RENAME CONSTRAINT uk_authority_name TO cx_authority_name;
CLUSTER authority USING cx_authority_name;

ALTER TABLE department RENAME CONSTRAINT uk_department_name TO cx_department_name;
CLUSTER department USING cx_department_name;

ALTER TABLE role RENAME CONSTRAINT uk_role_name TO cx_role_name;
CLUSTER role USING cx_role_name;

-- Список пользователей выводится с сортировкой по фамилии, поэтому кластеризованнный индекс основан на ней
CREATE INDEX cx_users_last_name ON users(last_name);
CLUSTER users USING cx_users_last_name;

