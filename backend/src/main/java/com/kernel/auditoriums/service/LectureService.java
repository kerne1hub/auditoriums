package com.kernel.auditoriums.service;

import com.kernel.auditoriums.entity.Lecture;
import com.kernel.auditoriums.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<List<Lecture>> getLectures() {
        return ResponseEntity.ok(lectureRepository.findAll());
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
