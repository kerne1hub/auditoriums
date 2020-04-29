CREATE TABLE IF NOT EXISTS `building` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(10) NOT NULL,
    `address` VARCHAR(255),
    PRIMARY KEY (`id`),
    UNIQUE KEY (`name`))
ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `auditorium` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(10) NOT NULL,
    `capacity` INT NOT NULL,
    `active` BOOLEAN NOT NULL,
    `building_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`building_id`) REFERENCES `building`(id) ON DELETE CASCADE,
    UNIQUE KEY (`name`))
ENGINE=InnoDB DEFAULT CHARSET=utf8;