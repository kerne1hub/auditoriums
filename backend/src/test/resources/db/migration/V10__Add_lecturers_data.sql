# password = test
INSERT INTO `user` (firstname, lastname, patronymic, email, login, password, user_type) VALUES
('Ирина', 'Тоцкая', 'Валерьевна', 'ivtotskaya@omgtu.ru', 'irish81', '$2a$04$.wKAmyRYEnP8g3dP/7AnXuSz7NIVKfZAlXUnNMxIbP95ybv9UBhqK', 'LECTURER');
INSERT INTO lecturer (id, position) VALUES (last_insert_id(), 'Assistant');

INSERT INTO `user` (firstname, lastname, patronymic, email, login, password, user_type) VALUES
('Владимир', 'Гудинов', 'Николаевич', 'gudinovvn@omgtu.ru', 'gudvn@omgtu.ru', 'GUDD1963', 'LECTURER');
INSERT INTO lecturer (id, position) VALUES (last_insert_id(), 'Senior Lecturer');
