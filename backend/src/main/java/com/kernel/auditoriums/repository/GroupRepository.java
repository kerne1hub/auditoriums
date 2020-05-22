package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findAllByNameContains(String keyword);
}
