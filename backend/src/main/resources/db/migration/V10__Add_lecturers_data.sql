INSERT INTO `user` (firstname, lastname, patronymic, email, login, password, user_type) VALUES
('Ирина', 'Тоцкая', 'Валерьевна', 'ivtotskaya@omgtu.ru', 'irish81', 'ase90FgOb', 'LECTURER');
INSERT INTO lecturer (id, position) VALUES (last_insert_id(), 'Assistant');

INSERT INTO `user` (firstname, lastname, patronymic, email, login, password, user_type) VALUES
('Владимир', 'Гудинов', 'Николаевич', 'gudinovvn@omgtu.ru', 'gudvn@omgtu.ru', 'GUDD1963', 'LECTURER');
INSERT INTO lecturer (id, position) VALUES (last_insert_id(), 'Senior Lecturer');