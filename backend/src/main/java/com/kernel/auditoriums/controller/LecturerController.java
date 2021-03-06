package com.kernel.auditoriums.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.kernel.auditoriums.entity.Lecturer;
import com.kernel.auditoriums.entity.utils.Views;
import com.kernel.auditoriums.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lecturers")
public class LecturerController {

    private final LecturerService lecturerService;

    @Autowired
    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @GetMapping
    @JsonView(Views.Default.class)
    public ResponseEntity<List<Lecturer>> getLecturers(@RequestParam(value = "name", required = false) String keyword) {
        return lecturerService.getLecturers(keyword);
    }

    @GetMapping("/{id}")
    @JsonView(Views.Lecturer.class)
    public ResponseEntity<Lecturer> getLecturerDetails(@PathVariable("id") Lecturer lecturer) {
        return lecturerService.getLecturer(lecturer);
    }

    @PostMapping
    @JsonView(Views.Default.class)
    public ResponseEntity<Lecturer> registerLecturer(@RequestBody Lecturer lecturer) {
        return lecturerService.createLecturer(lecturer);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Default.class)
    public ResponseEntity<Lecturer> editLecturerDetails(@AuthenticationPrincipal Authentication authentication, @PathVariable("id") Lecturer lecturerFromDb,
                                                        @RequestBody Lecturer lecturer) {
        return lecturerService.editLecturer(authentication.getName(), lecturerFromDb, lecturer);
    }

    @DeleteMapping("/{id}")
    public void deleteLecturer(@PathVariable("id") Lecturer lecturerFromDb) {
        lecturerService.deleteLecturer(lecturerFromDb);
    }
}
