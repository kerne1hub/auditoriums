INSERT INTO `building` (name, address) VALUES ('1', 'omgtu:1');
INSERT INTO `auditorium` (name, capacity, active, building_id) VALUES ('1-201', 30, false, last_insert_id());

INSERT INTO `building` (name, address) VALUES ('8', 'omgtu:8');
INSERT INTO `auditorium` (name, capacity, active, building_id) VALUES ('8-207', 28, true, last_insert_id());

INSERT INTO `building` (name, address) VALUES ('Г', 'omgtu:m');
INSERT INTO `auditorium` (name, capacity, active, building_id) VALUES ('Г-301', 24, false, last_insert_id());