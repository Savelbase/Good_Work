ALTER TABLE users DROP CONSTRAINT IF EXISTS uk_users_email;
ALTER TABLE role DROP CONSTRAINT IF EXISTS cx_role_name;
ALTER TABLE activity DROP CONSTRAINT IF EXISTS cx_activity_name;
ALTER TABLE country DROP CONSTRAINT IF EXISTS uk_country_name;
ALTER TABLE department DROP CONSTRAINT IF EXISTS cx_department_name;