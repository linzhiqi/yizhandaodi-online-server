create database yzdd_question;
create user 'questionadmin'@'localhost' identified by '';
GRANT ALL PRIVILEGES ON yzdd_question.* TO 'questionadmin'@'localhost';

USE yzdd_question;
Create table question_repo(
id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
`question` VARCHAR(255) NOT NULL,
`answers` VARCHAR(255) NOT NULL,
`checkType` SMALLINT DEFAULT 0,
`difficulty` SMALLINT NOT NULL,
`category` SMALLINT NOT NULL,
`episode` CHAR(10)
) ENGINE = MYISAM default CHARACTER SET utf8;
