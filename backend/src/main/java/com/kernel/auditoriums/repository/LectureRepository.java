package com.kernel.auditoriums.repository;

import com.kernel.auditoriums.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findAllByAuditoriumIdInAndDateBetweenOrderByDate(Collection<Integer> auditoriumIds, Date start, Date end);
    List<Lecture> findAllByGroupIdAndDateBetweenOrderByDate(Long groupId, Date start, Date end);
    List<Lecture> findAllByDateBetweenOrderByDate(Date startWeekDate, Date endWeekDate);
    List<Lecture> findAllByLecturerIdAndGroupIdAndDateBetweenOrderByDate(Long lecturerId, Long groupId, Date startWeekDate, Date endWeekDate);
    List<Lecture> findAllByLecturerIdAndDateBetweenOrderByDate(Long lecturerId, Date startWeekDate, Date endWeekDate);
    List<Lecture> findAllByLecturerIdIsNullAndDateBetweenOrderByDate(Date startWeekDate, Date endWeekDate);
    List<Lecture> findAllByAuditorium_BuildingIdAndDateBetweenOrderByDate(Integer buildingId, Date startWeekDate, Date endWeekDate);
}
