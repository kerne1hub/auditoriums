package com.kernel.auditoriums.service;

import com.kernel.auditoriums.entity.*;
import com.kernel.auditoriums.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.*;

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
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        return calendar.getTime();
    }

    public ResponseEntity<List<Lecture>> getLectures(boolean isUndefined, Integer buildingId, Long groupId, Long lecturerId, Date date) {
        Date startWeekDate;
        Date endWeekDate;

        if (date == null) {
            startWeekDate = setDayOfWeek(new Date(), Calendar.MONDAY);
            endWeekDate = setDayOfWeek(new Date(), Calendar.SUNDAY);
        } else {
            startWeekDate = setDayOfWeek(date, Calendar.MONDAY);
            endWeekDate = setDayOfWeek(date, Calendar.SUNDAY);
        }

        System.out.println("Get lectures between " + startWeekDate + " and " + endWeekDate);

        if (isUndefined) {
            return ResponseEntity.ok(lectureRepository.findAllByLecturerIdIsNullAndDateBetweenOrderByDate(startWeekDate, endWeekDate));
        }

        Lecture example = Lecture.builder()
                .auditorium(Auditorium.builder().building(Building.builder().id(buildingId).build()).build())
                .group(Group.builder().id(groupId).build())
                .lecturer(Lecturer.builder().id(lecturerId).build())
                .build();

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnorePaths("auditorium.capacity", "auditorium.active");

        return ResponseEntity.ok(lectureRepository.findAll(getSpecFromDatesAndExample(startWeekDate, endWeekDate, Example.of(example, matcher))));
    }

    private Specification<Lecture> getSpecFromDatesAndExample(Date from, Date to, Example<Lecture> example) {
        return (Specification<Lecture>) (root, query, builder) -> {
            final List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.between(root.get("date"), from, to));
            query.orderBy(builder.asc(root.get("date")));
            predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, example));

            return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public ResponseEntity<Lecture> getLectureDetails(Lecture lecture) {
        if (lecture != null) {
            return ResponseEntity.ok(lecture);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Lecture> createLecture(Lecture dto) {
        System.out.println("Create lecture on: " + dto.getDate());
        return createLectureEntity(dto, dto);
    }

    private ResponseEntity<Lecture> createLectureEntity(Lecture dto, Lecture entity) {
        entity.setDate(dto.getDate());
        entity.setAuditorium(auditoriumRepository.getOne(dto.getAuditoriumId()));
        entity.setGroup(groupRepository.getOne(dto.getGroupId()));
        entity.setLecturer(dto.getLecturerId() != null? lecturerRepository.getOne(dto.getLecturerId()): null);
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
