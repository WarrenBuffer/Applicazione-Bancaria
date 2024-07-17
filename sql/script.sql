-- DA ADMIN
create user 'project_admin'@'localhost' identified by 'pass';
create database applicazione_bancaria;
GRANT ALL PRIVILEGES
ON applicazione_bancaria.*
TO 'project_admin'@'localhost';
