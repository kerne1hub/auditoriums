package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Query("from Lecture l where l.auditoriumId in :auditoriumIds and date(l.date) between :start and :end")
    List<Lecture> findAllByAuditoriumIdInAndDate(Collection<Integer> auditoriumIds, Date start, Date end);
}
