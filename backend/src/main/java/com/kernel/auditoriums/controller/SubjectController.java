package com.kernel.auditoriums.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.kernel.auditoriums.entity.Subject;
import com.kernel.auditoriums.entity.utils.Views;
import com.kernel.auditoriums.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    @JsonView(Views.Default.class)
    public ResponseEntity<List<Subject>> getSubjects() {
        return subjectService.getSubjects();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Subject.class)
    public ResponseEntity<Subject> getSubjectDetails(@PathVariable("id") Subject subject) {
        return subjectService.getSubjectDetails(subject);
    }

    @PostMapping
    @JsonView(Views.Default.class)
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        return subjectService.createSubject(subject);
    }

    @DeleteMapping("/{id}")
    public void deleteSubject(@PathVariable("id") Subject subject) {
        subjectService.deleteSubject(subject);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Subject.class)
    public ResponseEntity<Subject> editSubject(@PathVariable("id") Subject subjectFromDb,
                                               @RequestBody Subject subject) {
        return subjectService.editSubject(subjectFromDb, subject);
    }
}
