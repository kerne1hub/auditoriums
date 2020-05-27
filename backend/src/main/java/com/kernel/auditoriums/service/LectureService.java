package com.kernel.auditoriums.service;

import com.kernel.auditoriums.entity.Lecture;
import com.kernel.auditoriums.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class LectureService {

    private final AuditoriumRepository auditoriumRepository;
    private final GroupRepository groupRepository;
    private final LectureRepository lectureRepository;
    private final LecturerRepository lecturerRepository;
    private final SubjectRepository subjectRepository;

    @Autowired
    public LectureService(AuditoriumRepository auditoriumRepository, GroupRepository groupRepository, LectureRepository repository, LecturerRepository lecturerRepository, SubjectRepository subjectRepository) {
        this.auditoriumRepository = auditoriumRepository;
        this.groupRepository = groupRepository;
        this.lectureRepository = repository;
        this.lecturerRepository = lecturerRepository;
        this.subjectRepository = subjectRepository;
    }

    private Date setDayOfWeek(Date date, int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        return calendar.getTime();
    }

    public ResponseEntity<List<Lecture>> getLectures(Long groupId, Date date) {
        if (groupId == null && date == null) {
            return ResponseEntity.ok(lectureRepository.findAll());
        }
        if (groupId != null && date == null) {
            return ResponseEntity.ok(lectureRepository.findAllByGroupId(groupId));
        }
        if (groupId != null) {
            Date startWeekDate = setDayOfWeek(date, Calendar.MONDAY);
            Date endWeekDate = setDayOfWeek(date, Calendar.SATURDAY);

            return ResponseEntity.ok(lectureRepository.findAllByGroupIdAndDateBetweenOrderByDate(groupId, startWeekDate, endWeekDate));
        }
        Date startWeekDate = setDayOfWeek(date, Calendar.MONDAY);
        Date endWeekDate = setDayOfWeek(date, Calendar.SATURDAY);
        return ResponseEntity.ok(lectureRepository.findAllByDateBetween(startWeekDate, endWeekDate));
    }

    public ResponseEntity<Lecture> getLectureDetails(Lecture lecture) {
        if (lecture != null) {
            return ResponseEntity.ok(lecture);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Lecture> createLecture(Lecture dto) {
        return createLectureEntity(dto, dto);
    }

    private ResponseEntity<Lecture> createLectureEntity(Lecture dto, Lecture entity) {
        entity.setDate(dto.getDate());
        entity.setAuditorium(auditoriumRepository.getOne(dto.getAuditoriumId()));
        entity.setGroup(groupRepository.getOne(dto.getGroupId()));
        entity.setLecturer(lecturerRepository.getOne(dto.getLecturerId()));
        entity.setSubject(subjectRepository.getOne(dto.getSubjectId()));

        return new ResponseEntity<>(lectureRepository.save(entity), HttpStatus.CREATED);
    }

    public ResponseEntity<Lecture> editLecture(Lecture lectureFromDb, Lecture lecture) {
        return createLectureEntity(lecture, lectureFromDb);
    }

    public void deleteLecture(Lecture lecture) {
        lectureRepository.delete(lecture);
    }
}
