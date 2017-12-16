ALTER USER postgres WITH PASSWORD 'postgres';

CREATE ROLE annotation_admin WITH LOGIN ENCRYPTED PASSWORD 'annotroot' CREATEDB;

CREATE DATABASE annotation WITH OWNER annotation_admin TEMPLATE template0 ENCODING 'UTF8';

GRANT ALL PRIVILEGES ON DATABASE annotation TO annotation_admin;

