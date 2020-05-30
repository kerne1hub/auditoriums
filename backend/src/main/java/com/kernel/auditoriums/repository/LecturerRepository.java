package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    List<Lecturer> findAllByLastNameContains(String keyword);
}
