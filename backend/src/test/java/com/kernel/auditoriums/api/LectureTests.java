package com.kernel.auditoriums.api;

import com.kernel.auditoriums.AuditoriumsApplication;
import com.kernel.auditoriums.entity.*;
import com.kernel.auditoriums.repository.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = AuditoriumsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LectureTests extends TestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private AuditoriumRepository auditoriumRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    @LocalServerPort
    private int port;

    private String url;

    @Before
    public void resetDb() {
        lectureRepository.deleteAll();
        lectureRepository.flush();
        subjectRepository.deleteAll();
        subjectRepository.flush();
        groupRepository.deleteAll();
        groupRepository.flush();
        lecturerRepository.deleteAll();
        lecturerRepository.flush();
        auditoriumRepository.deleteAll();
        auditoriumRepository.flush();
        buildingRepository.deleteAll();
        buildingRepository.flush();
        url = "http://localhost:" + port + "/api/";
    }

    @Test
    public void testCreateLecture() {
        int buildingId = createBuilding(restTemplate, url, "1").getBody().getId();

        ResponseEntity<Auditorium> response1 = createAuditorium(restTemplate, url, "1-301", 25, true, buildingId);
        Integer auditoriumId = response1.getBody().getId();

        ResponseEntity<Lecturer> response2 = createLecturer(restTemplate, url, "Dmitry", "Petrov", "Alexandrovich",
                "pdarus@mail.ru", "peality", "d$8Fk4v2", "Assistant");
        Long lecturerId = response2.getBody().getId();

        ResponseEntity<Subject> response3 = createSubject(restTemplate, url, "ТОАУ");
        Integer subjectId = response3.getBody().getId();

        ResponseEntity<Group> response4 = createGroup(restTemplate, url,"АТП-191");
        Long groupId = response4.getBody().getId();

        ResponseEntity<Lecture> response5 = createLecture(restTemplate, url, lecturerId, subjectId, groupId, auditoriumId);
        assertThat(response5.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response5.getBody().getId(), notNullValue());
        assertThat(response5.getBody().getGroup(), notNullValue());
        assertThat(response5.getBody().getSubject(), notNullValue());
        assertThat(response5.getBody().getAuditorium(), notNullValue());
        assertThat(response5.getBody().getLecturer(), notNullValue());
    }

    @Test
    public void testGetLectures() {
        int building1Id = createBuilding(restTemplate, url, "1").getBody().getId();

        ResponseEntity<Auditorium> response1 = createAuditorium(restTemplate, url, "1-301", 25, true, building1Id);
        Integer auditorium1Id = response1.getBody().getId();
        ResponseEntity<Lecturer> response2 = createLecturer(restTemplate, url, "Dmitry", "Petrov", "Alexandrovich",
                "pdarus@mail.ru", "peality", "d$8Fk4v2", "Assistant");
        Long lecturer1Id = response2.getBody().getId();
        ResponseEntity<Subject> response3 = createSubject(restTemplate, url, "ТОАУ");
        Integer subject1Id = response3.getBody().getId();
        ResponseEntity<Group> response4 = createGroup(restTemplate, url,"АТП-191");
        Long group1Id = response4.getBody().getId();
        ResponseEntity<Lecture> response5 = createLecture(restTemplate, url, lecturer1Id, subject1Id, group1Id, auditorium1Id);
        assertThat(response5.getStatusCode(), is(HttpStatus.CREATED));


        ResponseEntity<Auditorium> response6 = createAuditorium(restTemplate, url, "1-303", 28, true, building1Id);
        Integer auditorium2Id = response6.getBody().getId();
        ResponseEntity<Lecturer> response7 = createLecturer(restTemplate, url,"Alexey", "Vasilev", "Nikolaevich",
                "avral283@mail.ru", "to4ch55", "kwork181", "Assistant");
        Long lecturer2Id = response7.getBody().getId();
        ResponseEntity<Subject> response8 = createSubject(restTemplate, url,"Algorithms");
        Integer subject2Id = response8.getBody().getId();
        ResponseEntity<Group> response9 = createGroup(restTemplate, url,"ПИ-191");
        Long group2Id = response9.getBody().getId();
        ResponseEntity<Lecture> response10 = createLecture(restTemplate, url, lecturer2Id, subject2Id, group2Id, auditorium2Id);
        assertThat(response10.getStatusCode(), is(HttpStatus.CREATED));


        ResponseEntity<List<Lecture>> response11 = getLectureList(restTemplate, url);
        List<Lecture> lectures = response11.getBody();
        assertThat(lectures, notNullValue());
        assertThat(lectures.size(), is(2));
        assertThat(lectures.get(0), notNullValue(Lecture.class));
        assertThat(lectures.get(1), notNullValue(Lecture.class));
    }
}
