package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
}
