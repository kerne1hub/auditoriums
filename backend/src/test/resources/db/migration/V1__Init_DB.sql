CREATE TABLE IF NOT EXISTS `auditorium` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(10) NOT NULL,
    `capacity` INT NOT NULL,
    `active` BOOLEAN NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`name`))
ENGINE=InnoDB DEFAULT CHARSET=utf8;