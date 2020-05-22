package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findAllByAuditoriumIdInAndDateBetween(Collection<Integer> auditoriumIds, Date start, Date end);
    List<Lecture> findAllByGroupIdAndDateBetweenOrderByDate(Long groupId, Date start, Date end);
    List<Lecture> findAllByGroupId(Long groupId);
    List<Lecture> findAllByDateBetween(Date startWeekDate, Date endWeekDate);
}
