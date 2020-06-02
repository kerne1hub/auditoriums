package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long>, JpaSpecificationExecutor<Lecture> {

    List<Lecture> findAllByAuditoriumIdInAndDateBetweenOrderByDate(Collection<Integer> auditoriumIds, Date start, Date end);
    List<Lecture> findAllByLecturerIdIsNullAndDateBetweenOrderByDate(Date startWeekDate, Date endWeekDate);
}
