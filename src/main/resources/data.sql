-- Students
INSERT INTO student (name, second_name, email, password) VALUES ('Josep', 'Roure', 'roure@tecnocampus.cat', '{bcrypt}$2a$10$0VGzG8lfiDXBnFTE98lfiOLtP4uh62wnE6iWs5.2AMrJ3G9k7XZqu');
INSERT INTO student (name, second_name, email, password) VALUES ('Sergi', 'Alvarez', 'alvarez@tecnocampus.cat', '{bcrypt}$2a$10$7PFxXn4TQRiut9jNcAl7AubQZUWWck/eML3TDaQtoZiWNEN6o.Ig6');

-- Subjects
INSERT INTO subject (code, name, ects, term, level) VALUES ('103114', 'Anglès Professional', 4, 'T1', 'L1');
INSERT INTO subject (code, name, ects, term, level) VALUES ('103112', 'Introducció als Computadors', 6, 'T1', 'L1');
INSERT INTO subject (code, name, ects, term, level) VALUES ('103113', 'Emprenedoria i Innovació', 4, 'T1', 'L1');
INSERT INTO subject (code, name, ects, term, level) VALUES ('103212', 'Enginyeria del Software', 4, 'T1', 'L2');
INSERT INTO subject (code, name, ects, term, level) VALUES ('103234', 'Matemàtica Discreta', 6, 'T3', 'L2');

-- Registrations
INSERT INTO registration(student, academic_year, date) VALUES ('roure@tecnocampus.cat', '2017/2018', parsedatetime('2017-09-01', 'yyyy-MM-dd'));
INSERT INTO convocation(date, student , subject, mark) VALUES (parsedatetime('2017-09-01', 'yyyy-MM-dd'), 'roure@tecnocampus.cat', '103114', 7.2);
INSERT INTO convocation(date, student , subject, mark) VALUES (parsedatetime('2017-09-01', 'yyyy-MM-dd'), 'roure@tecnocampus.cat', '103112',  10.0);
INSERT INTO convocation(date, student , subject, mark) VALUES (parsedatetime('2017-09-01', 'yyyy-MM-dd'), 'roure@tecnocampus.cat', '103113',  4);


/*INSERT INTO registration(student, academic_year, date) VALUES ('roure@tecnocampus.cat', '2018/2019', parsedatetime('2018-09-01', 'yyyy-MM-dd'));
INSERT INTO convocation(date, student , subject) VALUES (parsedatetime('2018-09-01', 'yyyy-MM-dd'), 'roure@tecnocampus.cat', '103113');
INSERT INTO convocation(date, student , subject) VALUES (parsedatetime('2018-09-01', 'yyyy-MM-dd'), 'roure@tecnocampus.cat', '103212');
INSERT INTO convocation(date, student , subject) VALUES (parsedatetime('2018-09-01', 'yyyy-MM-dd'), 'roure@tecnocampus.cat', '103234');
*/

-- Authorities
INSERT INTO authorities (email, role) VALUES ('roure@tecnocampus.cat', 'ROLE_USER');
INSERT INTO authorities (email, role) VALUES ('roure@tecnocampus.cat', 'ROLE_ADMIN');
INSERT INTO authorities (email, role) VALUES ('alvarez@tecnocampus.cat', 'ROLE_USER');
