CREATE TABLE IF NOT EXISTS `lecture` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `date` DATETIME NOT NULL,
    `lecturer_id` BIGINT,
    `group_id` BIGINT NOT NULL,
    `subject_id` INT NOT NULL,
    `auditorium_id` INT,
    PRIMARY KEY (`id`),
    KEY (`date`),
    FOREIGN KEY (`lecturer_id`) REFERENCES `lecturer`(id) ON DELETE SET NULL,
    FOREIGN KEY (`group_id`) REFERENCES `group`(id) ON DELETE CASCADE,
    FOREIGN KEY (`subject_id`) REFERENCES `subject`(id) ON DELETE CASCADE,
    FOREIGN KEY (`auditorium_id`) REFERENCES auditorium(id) ON DELETE SET NULL)
    ENGINE=InnoDB DEFAULT CHARSET=utf8;