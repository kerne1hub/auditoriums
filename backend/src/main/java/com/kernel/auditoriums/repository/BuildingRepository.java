package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuildingRepository extends JpaRepository<Building, Integer> {
    List<Building> findAllByNameContains(String keyword);
}
