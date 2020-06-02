package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AuditoriumRepository extends JpaRepository<Auditorium, Integer> {

    @Query("select new Auditorium (a.id, a.name, a.capacity, a.active, b) from Auditorium a join Building b on b.id = a.buildingId where a.building.id = :buildingId")
    List<Auditorium> findAllByBuildingIdWithBuilding(int buildingId);

    @Query("select new Auditorium (a.id, a.name, a.capacity, a.active, a.buildingId) from Auditorium a where a.buildingId = :buildingId order by a.name")
    List<Auditorium> findAllByBuildingIdCustom(int buildingId);

    @Query("select a.id, a.name from Auditorium a where a.name like :keyword escape ''")
    List<Auditorium> findAllByNameContains(String keyword);

    List<Auditorium> findAllByBuildingIdAndLecturesDateBetween(int buildingId, Date startWeek, Date endWeek);
}
