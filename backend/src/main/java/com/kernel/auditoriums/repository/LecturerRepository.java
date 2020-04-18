package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
}
