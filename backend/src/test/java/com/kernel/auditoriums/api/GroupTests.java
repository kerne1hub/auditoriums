package com.kernel.auditoriums.api;

import com.kernel.auditoriums.AuditoriumsApplication;
import com.kernel.auditoriums.entity.Group;
import com.kernel.auditoriums.entity.Lecturer;
import com.kernel.auditoriums.repository.GroupRepository;
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
public class GroupTests extends TestBase{
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @LocalServerPort
    private int port;

    private String url;

    @Before
    public void resetDb() {
        groupRepository.deleteAll();
        groupRepository.flush();
        lecturerRepository.deleteAll();
        lecturerRepository.flush();

        url = "http://localhost:" + port + "/api/groups";

        Lecturer user = new Lecturer("User", "Test", null, "user@mail.cc", "login", "password", "Lecturer");
        registerAndAuth(passwordEncoder, user, restTemplate, port);
    }

    @Test
    public void testCreateGroup() {
        String groupName = "АТП-191";
        ResponseEntity<Group> response = createGroup(restTemplate, url, groupName);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is(groupName));
    }

    @Test
    public void testGetGroups() {
        String groupName1 = "АТП-191";
        ResponseEntity<Group> response1 = createGroup(restTemplate, url, groupName1);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        String groupName2 = "ИВТ-172";
        ResponseEntity<Group> response2 = createGroup(restTemplate, url, groupName2);
        assertThat(response2.getStatusCode(), is(HttpStatus.CREATED));


        ResponseEntity<List<Group>> response3 = getEntityList(restTemplate, url, new ParameterizedTypeReference<List<Group>>() {});
        List<Group> groups = response3.getBody();
        assertThat(groups, notNullValue());
        assertThat(groups.size(), is(2));
        assertThat(groups.get(0).getName(), is(groupName1));
        assertThat(groups.get(1).getName(), is(groupName2));
    }

    @Test
    public void testGetGroupDetails() {
        String name = "АТП-191";
        ResponseEntity<Group> response1 = createGroup(restTemplate, url, name);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        long id = response1.getBody().getId();
        ResponseEntity<Group> response2 = getEntity(restTemplate, url, id, Group.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), notNullValue());
        assertThat(response2.getBody().getName(), is(name));
    }

    @Test
    public void testDeleteGroup() {
        ResponseEntity<Group> response1 = createGroup(restTemplate, url, "АТП-191");
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        long id = response1.getBody().getId();
        ResponseEntity<Void> response2 = deleteEntity(restTemplate, url, id);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), nullValue());

        ResponseEntity<Group> response3 = getEntity(restTemplate, url, id, Group.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
}
