package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
