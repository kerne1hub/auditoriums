package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriumRepository extends JpaRepository<Auditorium, Integer> {
}
