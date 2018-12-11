DROP TABLE if EXISTS student;
CREATE TABLE student
(
  email VARCHAR (100) PRIMARY KEY,
  name VARCHAR (255),
  second_name VARCHAR (255),
  password VARCHAR(70) NOT NULL,
  enabled TINYINT NOT NULL DEFAULT 1
);

DROP TABLE if EXISTS subject;
CREATE TABLE subject
(
  code VARCHAR (10) PRIMARY KEY,
  name VARCHAR (255),
  ects integer ,
  term VARCHAR (5) NOT NULL,
  level VARCHAR(5) NOT NULL
);

DROP TABLE if EXISTS registration;
CREATE TABLE registration (
  student VARCHAR (100) NOT NULL,
  academic_year VARCHAR (9),
  date DATE NOT NULL ,
  FOREIGN KEY (student) REFERENCES student(email),
  PRIMARY KEY (student, date)
);

DROP TABLE IF EXISTS convocation;
CREATE TABLE convocation (
  date DATE NOT NULL ,
  student VARCHAR (100) NOT NULL,
  subject VARCHAR (10) NOT NULL,
  mark DOUBLE DEFAULT -1,
  FOREIGN KEY (subject) REFERENCES subject(code),
  FOREIGN KEY (student) REFERENCES registration(student),
  FOREIGN KEY (date) REFERENCES registration(date),
  PRIMARY KEY (subject, student, date)
);

DROP TABLE if EXISTS authorities;
CREATE TABLE authorities (
  authority_id int(11) NOT NULL AUTO_INCREMENT,
  email varchar(45) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (authority_id),
  UNIQUE KEY uni_username_role (role,email),
  CONSTRAINT fk_username FOREIGN KEY (email) REFERENCES student (email));
