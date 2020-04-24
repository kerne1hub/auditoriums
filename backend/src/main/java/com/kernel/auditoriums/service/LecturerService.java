package com.kernel.auditoriums.service;

import com.kernel.auditoriums.entity.Lecturer;
import com.kernel.auditoriums.repository.LecturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LecturerService {

    private final LecturerRepository repository;

    @Autowired
    public LecturerService(LecturerRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<List<Lecturer>> getLecturers() {
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<Lecturer> getLecturer(Lecturer lecturer) {
        if (lecturer != null) {
            return ResponseEntity.ok(lecturer);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Lecturer> createLecturer(Lecturer lecturer) {
        Lecturer savedLecturer = repository.save(lecturer);
        return new ResponseEntity<>(savedLecturer, HttpStatus.CREATED);
    }
}
