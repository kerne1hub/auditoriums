CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `firstname` VARCHAR(255) NOT NULL,
    `lastname` VARCHAR(255) NOT NULL,
    `patronymic` VARCHAR(255),
    `email` VARCHAR(255) NOT NULL,
    `login` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `user_type` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY (`email`),
    UNIQUE KEY (`login`))
ENGINE=InnoDB DEFAULT CHARSET=utf8;