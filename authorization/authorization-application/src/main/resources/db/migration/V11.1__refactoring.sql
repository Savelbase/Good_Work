DELETE
FROM role_authority
WHERE authority_id = '4872209a-f29d-4fe5-9244-27179c7fcb04';
DELETE
FROM role_authority
WHERE authority_id = '2597e5d8-7bd0-46bc-a17b-f810ecb0d37f';

DELETE
FROM authority
WHERE id = '4872209a-f29d-4fe5-9244-27179c7fcb04';
DELETE
FROM authority
WHERE id = '2597e5d8-7bd0-46bc-a17b-f810ecb0d37f';

UPDATE authority
SET name='ADD_EMPLOYEE_TO_DEPARTMENT'
WHERE id = '41a15085-7b43-406a-8e8c-9c2952e93e45';
UPDATE authority
SET name='SETTINGS'
WHERE id = 'e84a5f75-ab97-414c-ac0f-7f187cafc29e';

INSERT INTO authority
VALUES ('ac541cf7-18cd-4583-916b-42d640d0443f', 'VIEW_ROLES');
INSERT INTO authority
VALUES ('cb069b8b-f0ab-484a-9943-a3f829f1919f', 'VIEW_DEPARTMENTS');


ALTER TABLE role
    ADD COLUMN deleted bool DEFAULT false;


ALTER TABLE users
    RENAME CONSTRAINT uk_user_email TO cx_users_email;
CLUSTER users USING cx_users_email;