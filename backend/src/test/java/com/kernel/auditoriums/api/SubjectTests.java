package com.kernel.auditoriums.api;

import com.kernel.auditoriums.AuditoriumsApplication;
import com.kernel.auditoriums.entity.Lecturer;
import com.kernel.auditoriums.entity.Subject;
import com.kernel.auditoriums.repository.LecturerRepository;
import com.kernel.auditoriums.repository.SubjectRepository;
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
public class SubjectTests extends TestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @LocalServerPort
    private int port;

    private String url;

    @Before
    public void resetDb() {
        subjectRepository.deleteAll();
        subjectRepository.flush();
        lecturerRepository.deleteAll();
        lecturerRepository.flush();

        url = "http://localhost:" + port + "/api/subjects";

        Lecturer user = new Lecturer("User", "Test", null, "user@mail.cc", "login", "password", "Lecturer");
        registerAndAuth(passwordEncoder, user, restTemplate, port);
    }
    
    @Test
    public void testCreateSubject() {
        String subjectName = "ТОАУ";
        ResponseEntity<Subject> response = createSubject(restTemplate, url, subjectName);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is(subjectName));
    }
    
    @Test
    public void testGetSubjects() {
        String subjectName1 = "ТОАУ";
        ResponseEntity<Subject> response1 = createSubject(restTemplate, url, subjectName1);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        String subjectName2 = "СУБД";
        ResponseEntity<Subject> response2 = createSubject(restTemplate, url, subjectName2);
        assertThat(response2.getStatusCode(), is(HttpStatus.CREATED));

        ResponseEntity<List<Subject>> response3 = getEntityList(restTemplate, url, new ParameterizedTypeReference<List<Subject>>() {});

        List<Subject> subjects = response3.getBody();

        assertThat(subjects, notNullValue());
        assertThat(subjects.size(), is(2));
        assertThat(subjects.get(0).getName(), is(subjectName2));
        assertThat(subjects.get(1).getName(), is(subjectName1));
    }

    @Test
    public void testGetSubjectDetails() {
        String subjectName = "СУБД";
        ResponseEntity<Subject> response1 = createSubject(restTemplate, url, subjectName);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        int id = response1.getBody().getId();
        ResponseEntity<Subject> response2 = getEntity(restTemplate, url, id, Subject.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), notNullValue());
        assertThat(response2.getBody().getName(), is(subjectName));
    }

    @Test
    public void testEditSubjectDetails() {
        String subjectName = "ТОАУ";
        ResponseEntity<Subject> response1 = createSubject(restTemplate, url, subjectName);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        Subject subject = response1.getBody();
        int id = response1.getBody().getId();
        subject.setName("ТАУ");
        ResponseEntity<Subject> response2 = editEntity(restTemplate, url, id, subject, Subject.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), notNullValue());
        assertThat(response2.getBody().getName(), is(subject.getName()));
    }

    @Test
    public void testDeleteSubject() {
        String subjectName1 = "ТОАУ";
        ResponseEntity<Subject> response1 = createSubject(restTemplate, url, subjectName1);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        int id = response1.getBody().getId();
        ResponseEntity<Void> response2 = deleteEntity(restTemplate, url, id);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), nullValue());

        ResponseEntity<Subject> response3 = getEntity(restTemplate, url, id, Subject.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }

}
