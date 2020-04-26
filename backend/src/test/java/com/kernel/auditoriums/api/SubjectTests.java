package com.kernel.auditoriums.api;

import com.kernel.auditoriums.AuditoriumsApplication;
import com.kernel.auditoriums.entity.Subject;
import com.kernel.auditoriums.repository.SubjectRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = AuditoriumsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SubjectTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SubjectRepository repository;

    @LocalServerPort
    private int port;

    private String url;

    @Before
    public void resetDb() {
        repository.deleteAll();
        repository.flush();
        url = "http://localhost:" + port + "/api/subjects";
    }
    
    @Test
    public void testCreateSubject() {
        Subject subject = new Subject("ТОАУ");
        ResponseEntity<Subject> response = restTemplate.postForEntity(url, subject, Subject.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is(subject.getName()));
    }
    
    @Test
    public void testGetSubjects() {
        Subject subject1 = new Subject("ТОАУ");
        ResponseEntity<Subject> response1 = restTemplate.postForEntity(url, subject1, Subject.class);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        Subject subject2 = new Subject("СУБД");
        ResponseEntity<Subject> response2 = restTemplate.postForEntity(url, subject2, Subject.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.CREATED));

        ResponseEntity<List<Subject>> response3 = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Subject>>() {
                });

        List<Subject> subjects = response3.getBody();

        assertThat(subjects, notNullValue());
        assertThat(subjects.size(), is(2));
        assertThat(subjects.get(0).getName(), is(subject2.getName()));
        assertThat(subjects.get(1).getName(), is(subject1.getName()));
    }

    @Test
    public void testGetSubjectDetails() {
        Subject subject = new Subject("СУБД");
        ResponseEntity<Subject> response1 = restTemplate.postForEntity(url, subject, Subject.class);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        int id = response1.getBody().getId();
        ResponseEntity<Subject> response2 = restTemplate.getForEntity(url + "/" + id, Subject.class);

        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), notNullValue());
        assertThat(response2.getBody().getName(), is(subject.getName()));
    }

    @Test
    public void testEditSubjectDetails() {
        Subject subject = new Subject("ТОАУ");
        ResponseEntity<Subject> response1 = restTemplate.postForEntity(url, subject, Subject.class);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        int id = response1.getBody().getId();
        subject.setName("ТАУ");
        ResponseEntity<Subject> response2 = restTemplate.exchange(url + "/" + id, HttpMethod.PUT,
                new HttpEntity<>(subject), Subject.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), notNullValue());
        assertThat(response2.getBody().getName(), is(subject.getName()));
    }

    @Test
    public void testDeleteSubject() {
        Subject subject = new Subject("ТОАУ");
        ResponseEntity<Subject> response1 = restTemplate.postForEntity(url, subject, Subject.class);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        int id = response1.getBody().getId();
        ResponseEntity<Void> response2 = restTemplate.exchange(url + "/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), nullValue());

        ResponseEntity<Subject> response3 = restTemplate.getForEntity(url + "/" + id, Subject.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

}
