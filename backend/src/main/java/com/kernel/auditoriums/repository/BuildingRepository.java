package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingRepository extends JpaRepository<Building, Integer> {
}
