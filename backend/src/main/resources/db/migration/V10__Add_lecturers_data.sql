INSERT INTO `user` (firstname, lastname, patronymic, email, login, password, user_type) VALUES
('Ирина', 'Тоцкая', 'Валерьевна', 'ivtotskaya@omgtu.ru', 'irish81', 'ase90FgOb', 'LECTURER');
INSERT INTO lecturer (id, position) VALUES (last_insert_id(), 'Assistant');

INSERT INTO `user` (firstname, lastname, patronymic, email, login, password, user_type) VALUES
('Владимир', 'Гудинов', 'Николаевич', 'gudinovvn@omgtu.ru', 'gudvn', 'GUDD1963', 'LECTURER');
INSERT INTO lecturer (id, position) VALUES (last_insert_id(), 'Senior Lecturer');

# password = wordpass
INSERT INTO `user` (firstname, lastname, patronymic, email, login, password, user_type) VALUES
('Константин', 'Федоров', 'Николаевич', 'feg3io@omgtu.ru', 'constFN', '$2a$04$VqDOq/85qqtgdcbSPEBm0.Fq3jBHpxb84fbKmWDqpV82aTO1S5Wjy', 'LECTURER');
INSERT INTO lecturer (id, position) VALUES (last_insert_id(), 'Lecturer');
