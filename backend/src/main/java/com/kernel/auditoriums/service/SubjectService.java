package com.kernel.auditoriums.service;

import com.kernel.auditoriums.entity.Subject;
import com.kernel.auditoriums.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository repository;

    @Autowired
    public SubjectService(SubjectRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<List<Subject>> getSubjects() {
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<Subject> getSubjectDetails(Subject subject) {
        if (subject != null) {
            return ResponseEntity.ok(subject);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Subject> createSubject(Subject subject) {
        Subject savedSubject = repository.save(subject);
        return new ResponseEntity<>(savedSubject, HttpStatus.CREATED);
    }

    public void deleteSubject(Subject subject) {
        repository.delete(subject);
    }

    public ResponseEntity<Subject> editSubject(Subject subjectFromDb, Subject subject) {
        subjectFromDb.setName(subject.getName());
        return ResponseEntity.ok(repository.save(subjectFromDb));
    }
}
