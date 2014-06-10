create database yzdd_account;
create user 'accountadmin'@'localhost' identified by '';
GRANT ALL PRIVILEGES ON yzdd_account.* TO 'accountadmin'@'localhost';

USE yzdd_account;
Create table account(
id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
`name` VARCHAR(255) NOT NULL UNIQUE,
email VARCHAR(255) NOT NULL UNIQUE,
`password` VARCHAR(255) NOT NULL,
win BIGINT default 0,
lose BIGINT default 0,
 INDEX (email)
) ENGINE = MYISAM default CHARACTER SET utf8;
