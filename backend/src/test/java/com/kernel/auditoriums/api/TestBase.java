package com.kernel.auditoriums.api;

import com.kernel.auditoriums.api.dto.AuthResponseDto;
import com.kernel.auditoriums.entity.*;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class TestBase {

    private String token;

    public long registerAndAuth(PasswordEncoder encoder, User user, TestRestTemplate restTemplate, int port) {
        String password = user.getPassword();
        ResponseEntity<Lecturer> userResponseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/lecturers", user, Lecturer.class);

        user.setPassword(password);
        ResponseEntity<AuthResponseDto> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/login", user, AuthResponseDto.class);
        token = responseEntity.getBody().getToken();
        return responseEntity.getBody().getUser().getId();
    }

//    Set JWT header
    private<T> HttpEntity<T> getEntityWithAuth(T entity) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(entity, headers);
    }

    protected ResponseEntity<Building> createBuilding(TestRestTemplate restTemplate, String url, String name) {
        Building building = new Building(name);
        HttpEntity<Building> entity = getEntityWithAuth(building);
        return restTemplate.postForEntity(url, entity, Building.class);
    }

    protected ResponseEntity<Auditorium> createAuditorium(TestRestTemplate restTemplate, String url, String name,
                                                                 int capacity, boolean isActive, int buildingId) {
        
        Auditorium auditorium = new Auditorium(name, capacity, isActive, buildingId);
        HttpEntity<Auditorium> entity = getEntityWithAuth(auditorium);
        return restTemplate.postForEntity(url, entity, Auditorium.class);
    }

    protected ResponseEntity<Lecturer> createLecturer(TestRestTemplate restTemplate, String url, Lecturer lecturer) {
        return restTemplate.postForEntity(url, lecturer, Lecturer.class);
    }

    protected ResponseEntity<Subject> createSubject(TestRestTemplate restTemplate, String url, String name) {
        
        Subject subject = new Subject(name);
        HttpEntity<Subject> entity = getEntityWithAuth(subject);
        return restTemplate.postForEntity(url, entity, Subject.class);
    }

    protected ResponseEntity<Group> createGroup(TestRestTemplate restTemplate, String url, String name) {
        Group group = new Group(name);
        HttpEntity<Group> entity = getEntityWithAuth(group);
        return restTemplate.postForEntity(url, entity, Group.class);
    }

//    WARN: Avoid creating lecture on Sunday
    protected ResponseEntity<Lecture> createLecture(TestRestTemplate restTemplate, String url, Long lecturerId,
                                                           Integer subjectId, Long groupId, Integer auditoriumId) {
        Date date = new Date();
        checkDay(date);
        Lecture lecture = new Lecture(date, lecturerId, subjectId, groupId, auditoriumId);
        HttpEntity<Lecture> entity = getEntityWithAuth(lecture);
        return restTemplate.postForEntity(url, entity, Lecture.class);
    }

    private void checkDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Omsk"));
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        }

        date.setTime(calendar.getTime().getTime());
        System.out.println("CalendarTime: " + calendar.toString());
    }

    protected ResponseEntity<List<Lecture>> getLectureList(TestRestTemplate restTemplate, String url) {
        return restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Lecture>>() {
                });
    }

    protected<T> ResponseEntity<T> editEntity(TestRestTemplate restTemplate, String url, long id, T entity, Class<T> entityClass) {
        HttpEntity<T> httpEntity = getEntityWithAuth(entity);
        return restTemplate.exchange(url + "/" + id, HttpMethod.PUT, httpEntity, entityClass);
    }

    protected<T> ResponseEntity<List<T>> getEntityList(TestRestTemplate restTemplate, String url, ParameterizedTypeReference<List<T>> typeReference ) {
        return restTemplate.exchange(url, HttpMethod.GET, null, typeReference);
    }

    protected<T> ResponseEntity<T> getEntity(TestRestTemplate restTemplate, String url, long id, Class<T> entityClass) {
        return restTemplate.getForEntity(url + "/" + id, entityClass);
    }

    protected ResponseEntity<Void> deleteEntity(TestRestTemplate restTemplate, String url, long id) {
        HttpEntity<Void> entity = getEntityWithAuth(null);
        return restTemplate.exchange(url + "/" + id, HttpMethod.DELETE, entity, Void.class);
    }
}
