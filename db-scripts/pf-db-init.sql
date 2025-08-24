DROP SCHEMA IF EXISTS `product_db`;
CREATE SCHEMA IF NOT EXISTS `product_db`;
USE `product_db`;

DROP USER IF EXISTS 'product_app'@'%';
DROP ROLE IF EXISTS  'product_app_role';
CREATE ROLE 'product_app_role';
GRANT ALL ON `product_db`.* TO 'product_app_role';
CREATE USER 'product_app'@'%' IDENTIFIED BY 'test1234';
GRANT 'product_app_role' TO 'product_app'@'%';
SET DEFAULT ROLE ALL TO 'product_app'@'%';