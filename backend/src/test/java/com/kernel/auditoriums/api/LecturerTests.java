package com.kernel.auditoriums.api;

import com.kernel.auditoriums.AuditoriumsApplication;
import com.kernel.auditoriums.entity.Lecturer;
import com.kernel.auditoriums.repository.LecturerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = AuditoriumsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LecturerTests extends TestBase{
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LecturerRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @LocalServerPort
    private int port;

    private String url;

    @Before
    public void resetDb() {
        repository.deleteAll();
        repository.flush();

        url = "http://localhost:" + port + "/api/lecturers";

        Lecturer user = new Lecturer("User", "Test", null, "user@mail.cc", "login", "password", "Lecturer");
        registerAndAuth(passwordEncoder, user, restTemplate, port);
    }

    @Test
    public void testCreateLecturer() {
        Lecturer lecturer = new Lecturer("Dmitry", "Petrov", "Alexandrovich",
                "pdarus@mail.ru", "peality", "d$8Fk4v2", "Assistant");
        ResponseEntity<Lecturer> response = createLecturer(restTemplate, url, lecturer);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getId(), notNullValue());

        lecturer.setId(response.getBody().getId());
        assertThat(response.getBody(), notNullValue(Lecturer.class));
    }

    @Test
//    One user already exists from registerAndAuth()
    public void testGetLecturers() {
        Lecturer lecturer1 = new Lecturer("Dmitry", "Petrov", "Alexandrovich",
                "pdarus@mail.ru", "peality", "d$8Fk4v2", "Assistant");
        ResponseEntity<Lecturer> response1 = createLecturer(restTemplate, url, lecturer1);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        Lecturer lecturer2 = new Lecturer("Andrey", "Baranov", "Mihailovich",
                "andro55@mail.ru", "berg55@yandex.ru", "sierre16", "Docent");
        ResponseEntity<Lecturer> response2 = createLecturer(restTemplate, url, lecturer2);
        assertThat(response2.getStatusCode(), is(HttpStatus.CREATED));

        ResponseEntity<List<Lecturer>> response3 = getEntityList(restTemplate, url, new ParameterizedTypeReference<List<Lecturer>>() {});
        List<Lecturer> lecturers = response3.getBody();
        assertThat(lecturers, notNullValue());
        assertThat(lecturers.size(), is(3));

        assertThat(lecturers.get(0), notNullValue(Lecturer.class));
        assertThat(lecturers.get(1), notNullValue(Lecturer.class));
    }

    @Test
    public void testGetLecturerDetails() {
        Lecturer lecturer = new Lecturer("Dmitry", "Petrov", "Alexandrovich",
                "pdarus@mail.ru", "peality", "d$8Fk4v2", "Assistant");
        ResponseEntity<Lecturer> response1 = createLecturer(restTemplate, url, lecturer);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        long id = response1.getBody().getId();
        ResponseEntity<Lecturer> response2 = getEntity(restTemplate, url, id, Lecturer.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), notNullValue());

        lecturer.setId(id);
        assertThat(response2.getBody(), notNullValue(Lecturer.class));
    }

    @Test
    public void testUpdateLecturerDetailsOk() {
        Lecturer lecturer = new Lecturer("Dmitry", "Petrov", "Alexandrovich",
                "pdarus@mail.ru", "peality", "d$8Fk4v2", "Assistant");
        long id = registerAndAuth(passwordEncoder, lecturer, restTemplate, port);

        lecturer.setPosition("Lecturer");

        ResponseEntity<Lecturer> response2 = editEntity(restTemplate, url, id, lecturer, Lecturer.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), notNullValue());
        assertThat(response2.getBody().getPosition(), is(lecturer.getPosition()));
    }

    @Test
    public void testUpdateLecturerDetailsWithNoPasswordFail() {
        Lecturer lecturer = new Lecturer("Dmitry", "Petrov", "Alexandrovich",
                "pdarus@mail.ru", "peality", "d$8Fk4v2", "Assistant");
        long id = registerAndAuth(passwordEncoder, lecturer, restTemplate, port);

        lecturer.setPosition("Lecturer");
        lecturer.setPassword("");

        ResponseEntity<Lecturer> response2 = editEntity(restTemplate, url, id, lecturer, Lecturer.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void testDeleteLecturer() {
        Lecturer lecturer = new Lecturer("Dmitry", "Petrov", "Alexandrovich",
                "pdarus@mail.ru", "peality", "d$8Fk4v2", "Assistant");
        ResponseEntity<Lecturer> response1 = createLecturer(restTemplate, url, lecturer);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        long id = response1.getBody().getId();
        ResponseEntity<Void> response2 = deleteEntity(restTemplate, url, id);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), nullValue());

        ResponseEntity<Lecturer> response3 = getEntity(restTemplate, url, id, Lecturer.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
}
