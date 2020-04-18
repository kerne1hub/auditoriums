package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
}
