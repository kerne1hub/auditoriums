package com.kernel.auditoriums.api;

import com.kernel.auditoriums.AuditoriumsApplication;
import com.kernel.auditoriums.entity.Group;
import com.kernel.auditoriums.repository.GroupRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
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
public class GroupTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private GroupRepository repository;

    @LocalServerPort
    private int port;

    private String url;

    @Before
    public void resetDb() {
        repository.deleteAll();
        repository.flush();
        url = "http://localhost:" + port + "/api/groups";
    }

    @Test
    public void testCreateGroup() {
        Group group = new Group("АТП-191");
        ResponseEntity<Group> response = restTemplate.postForEntity(url, group, Group.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is(group.getName()));
    }

    @Test
    public void testGetGroups() {
        Group group1 = new Group("АТП-191");
        ResponseEntity<Group> response1 = restTemplate.postForEntity(url, group1, Group.class);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        Group group2 = new Group("ИВТ-172");
        ResponseEntity<Group> response2 = restTemplate.postForEntity(url, group2, Group.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.CREATED));

        ResponseEntity<List<Group>> response3 = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Group>>() {
                });

        List<Group> groups = response3.getBody();

        assertThat(groups, notNullValue());
        assertThat(groups.size(), is(2));
        assertThat(groups.get(0).getName(), is(group1.getName()));
        assertThat(groups.get(1).getName(), is(group2.getName()));
    }

    @Test
    public void testGetGroupDetails() {
        Group group = new Group("АТП-191");
        ResponseEntity<Group> response1 = restTemplate.postForEntity(url, group, Group.class);

        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        long id = response1.getBody().getId();
        ResponseEntity<Group> response2 = restTemplate.getForEntity(url + "/" + id, Group.class);

        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), notNullValue());
        assertThat(response2.getBody().getName(), is(group.getName()));
    }

    @Test
    public void testDeleteGroup() {
        Group group = new Group("АТП-191");
        ResponseEntity<Group> response1 = restTemplate.postForEntity(url, group, Group.class);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        long id = response1.getBody().getId();
        ResponseEntity<Void> response2 = restTemplate.exchange(url + "/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), nullValue());

        ResponseEntity<Group> response3 = restTemplate.getForEntity(url + "/" + id, Group.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
}
