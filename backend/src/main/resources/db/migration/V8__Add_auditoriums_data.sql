INSERT INTO `building` (id, name, address) VALUES (1, '1', 'omgtu:1');
INSERT INTO `auditorium` (id, name, capacity, active, building_id) VALUES (1, '1-201', 30, true, 1),
                                                                      (2, '1-203', 24, true, 1),
                                                                      (3, '1-205', 25, true, 1),
                                                                      (4, '1-207', 32, true, 1),
                                                                      (5, '1-208', 16, false, 1),
                                                                      (6, '1-209', 23, true, 1),
                                                                      (7, '1-211', 22, false, 1);


INSERT INTO `building` (name, address) VALUES ('6', 'omgtu:6');
INSERT INTO `auditorium` (name, capacity, active, building_id) VALUES ('6-409', 30, false, last_insert_id());

INSERT INTO `building` (name, address) VALUES ('8', 'omgtu:8');
INSERT INTO `auditorium` (name, capacity, active, building_id) VALUES ('8-207', 28, true, last_insert_id());

INSERT INTO `building` (name, address) VALUES ('Г', 'omgtu:m');
INSERT INTO `auditorium` (name, capacity, active, building_id) VALUES ('Г-301', 24, false, last_insert_id());
