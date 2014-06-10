Create table question_tmp(
`question` VARCHAR(255) NOT NULL,
`answers` VARCHAR(255) NOT NULL,
`difficulty` SMALLINT NOT NULL,
`category` SMALLINT NOT NULL
) ENGINE = MYISAM default CHARACTER SET utf8;

truncate table question_tmp;

load data local infile '/home/dzlin/2012-07-06.txt'
INTO TABLE question_tmp
CHARACTER SET utf8
FIELDS TERMINATED BY '\t';

insert into question_repo (question,answers,difficulty,category,episode) 
select question,answers,difficulty,category,"2012-07-06" from question_tmp;



