package com.kernel.auditoriums.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.kernel.auditoriums.entity.Lecture;
import com.kernel.auditoriums.entity.utils.Views;
import com.kernel.auditoriums.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping
    @JsonView(Views.Lecture.class)
    public ResponseEntity<List<Lecture>> getLectures() {
        return lectureService.getLectures();
    }

    @GetMapping("/{id}")
    @JsonView(Views.Lecture.class)
    public ResponseEntity<Lecture> getLectureDetails(@PathVariable("id") Lecture lecture) {
        return lectureService.getLectureDetails(lecture);
    }

    @PostMapping
    @JsonView(Views.Lecture.class)
    public ResponseEntity<Lecture> createLecture(@RequestBody Lecture lecture) {
        return lectureService.createLecture(lecture);
    }

    @PutMapping("/{id}")
    @JsonView(Views.Lecture.class)
    public ResponseEntity<Lecture> editLecture(@PathVariable("id") Lecture lectureFromDb,
                                               @RequestBody Lecture lecture) {
        return lectureService.editLecture(lectureFromDb, lecture);
    }

    @DeleteMapping("/{id}")
    public void deleteLecture(@PathVariable("id") Lecture lecture) {
        lectureService.deleteLecture(lecture);
    }

}
