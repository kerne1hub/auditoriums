package com.kernel.auditoriums.api;

import com.kernel.auditoriums.entity.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public class TestBase {

    protected ResponseEntity<Auditorium> createAuditorium(TestRestTemplate restTemplate, String url, String name,
                                                                 int capacity, boolean isActive) {
        
        Auditorium auditorium = new Auditorium(name, capacity, isActive);
        return restTemplate.postForEntity(url + "auditoriums", auditorium, Auditorium.class);
    }

    protected ResponseEntity<List<Auditorium>> getAuditoriumList(TestRestTemplate restTemplate, String url) {
        
        return restTemplate.exchange(url + "auditoriums", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Auditorium>>() {
                });
    }

    protected ResponseEntity<Lecturer> createLecturer(TestRestTemplate restTemplate, String url, String firstName,
                                                             String lastName, String patronymic, String email,
                                                             String login, String password, String position) {
        
        Lecturer lecturer = new Lecturer(firstName, lastName, patronymic, email, login, password, position);
        return restTemplate.postForEntity(url + "lecturers", lecturer, Lecturer.class);
    }

    protected ResponseEntity<Subject> createSubject(TestRestTemplate restTemplate, String url, String name) {
        
        Subject subject = new Subject(name);
        return restTemplate.postForEntity(url + "subjects", subject, Subject.class);
    }

    protected ResponseEntity<Group> createGroup(TestRestTemplate restTemplate, String url, String name) {
        
        Group group = new Group(name);
        return restTemplate.postForEntity(url + "groups", group, Group.class);
    }

    protected ResponseEntity<Lecture> createLecture(TestRestTemplate restTemplate, String url, Long lecturerId,
                                                           Integer subjectId, Long groupId, Integer auditoriumId) {
        Lecture lecture = new Lecture(new Date(), lecturerId, subjectId, groupId, auditoriumId);
        return restTemplate.postForEntity(url + "lectures", lecture, Lecture.class);
    }

    protected ResponseEntity<List<Lecture>> getLectureList(TestRestTemplate restTemplate, String url) {
        return restTemplate.exchange(url + "lectures", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Lecture>>() {
                });
    }
}
