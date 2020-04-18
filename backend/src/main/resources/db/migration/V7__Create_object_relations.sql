CREATE TABLE IF NOT EXISTS `auditorium_lecture` (
    `auditorium_id` INT(11),
    `lecture_id` BIGINT(20) NOT NULL,
    FOREIGN KEY (`auditorium_id`) REFERENCES `auditorium`(id) ON DELETE SET NULL,
    FOREIGN KEY (`lecture_id`) REFERENCES `lecture`(id) ON DELETE CASCADE)
ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `group_subject` (
    `group_id` BIGINT(20) NOT NULL,
    `subject_id` INT NOT NULL,
    FOREIGN KEY (`group_id`) REFERENCES `group`(id) ON DELETE CASCADE,
    FOREIGN KEY (`subject_id`) REFERENCES `subject`(id) ON DELETE CASCADE)
ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `group_lecturer` (
    `group_id` BIGINT(20) NOT NULL,
    `lecturer_id` BIGINT NOT NULL,
    FOREIGN KEY (`group_id`) REFERENCES `group`(id) ON DELETE CASCADE,
    FOREIGN KEY (`lecturer_id`) REFERENCES `lecturer`(id) ON DELETE CASCADE)
ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `lecturer_subject` (
    `lecturer_id` BIGINT(20) NOT NULL,
    `subject_id` INT NOT NULL,
    FOREIGN KEY (`lecturer_id`) REFERENCES `lecturer`(id) ON DELETE CASCADE,
    FOREIGN KEY (`subject_id`) REFERENCES `subject`(id) ON DELETE CASCADE)
ENGINE=InnoDB DEFAULT CHARSET=utf8;