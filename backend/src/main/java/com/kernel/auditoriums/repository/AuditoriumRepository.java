package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Auditorium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public interface AuditoriumRepository extends JpaRepository<Auditorium, Integer> {
    List<Auditorium> findAllByBuildingId(int buildingId);
//    @Query(value = "from Auditorium a inner join Lecture l on l.auditorium.id = l.auditorium.id where a.building.id = :buildingId and date(l.date) = :lectureDate")
//    List<Auditorium> findAllByBuildingIdAndLecturesDate(@Param("buildingId") int buildingId,
//                                                        @Param("lectureDate") @Temporal(TemporalType.DATE) Date date);
    @Query("select new Auditorium (a.id, a.name, a.capacity, a.active, a.buildingId) from Auditorium a where a.buildingId = :buildingId order by a.name")
    List<Auditorium> findAllByBuildingIdCustom(int buildingId);
}
