package com.kernel.auditoriums.api;

import com.kernel.auditoriums.AuditoriumsApplication;
import com.kernel.auditoriums.entity.Auditorium;
import com.kernel.auditoriums.repository.AuditoriumRepository;
import com.kernel.auditoriums.repository.BuildingRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
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
public class AuditoriumTests extends TestBase {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuditoriumRepository auditoriumRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    @LocalServerPort
    protected int port;

    private String url;

    @Before
    public void setUp() {
        auditoriumRepository.deleteAll();
        auditoriumRepository.flush();
        buildingRepository.deleteAll();
        buildingRepository.flush();
        url = "http://localhost:" + port + "/api/";
    }

    @Test
    public void testCreateAuditorium() {
        int buildingId = createBuilding(restTemplate, url, "1").getBody().getId();

        String auditoriumName = "1-303";
        ResponseEntity<Auditorium> response = createAuditorium(restTemplate, url, auditoriumName, 25, true, buildingId);
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is(auditoriumName));
    }

    @Test
    public void testGetAuditoriums() {
        int building1Id = createBuilding(restTemplate, url, "1").getBody().getId();
        int building2Id = createBuilding(restTemplate, url, "6").getBody().getId();

        String auditorium1Name = "1-301";
        ResponseEntity<Auditorium> response1 = createAuditorium(restTemplate, url, auditorium1Name, 25, true, building1Id);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        String auditorium2Name = "6-407";
        ResponseEntity<Auditorium> response2 = createAuditorium(restTemplate, url, auditorium2Name, 26, true, building2Id);
        assertThat(response2.getStatusCode(), is(HttpStatus.CREATED));

        ResponseEntity<List<Auditorium>> response3 = getAuditoriumList(restTemplate, url);
        List<Auditorium> auditoriums = response3.getBody();
        assertThat(auditoriums, notNullValue());
        assertThat(auditoriums.size(), is(2));
        assertThat(auditoriums.get(0).getName(), is(auditorium1Name));
        assertThat(auditoriums.get(1).getName(), is(auditorium2Name));
    }

    @Test
    public void testGetAuditoriumDetails() {
        int buildingId = createBuilding(restTemplate, url, "1").getBody().getId();

        String auditorium1Name = "1-301";
        ResponseEntity<Auditorium> response1 = createAuditorium(restTemplate, url, auditorium1Name, 25, true, buildingId);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        int id = response1.getBody().getId();
        ResponseEntity<Auditorium> response2 = restTemplate.getForEntity(url + "auditoriums/" + id, Auditorium.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), notNullValue());
        assertThat(response2.getBody().getName(), is(auditorium1Name));
    }

    @Test
    public void testUpdateAuditoriumDetailsOk() {
        int buildingId = createBuilding(restTemplate, url, "1").getBody().getId();

        String auditorium1Name = "1-301";
        ResponseEntity<Auditorium> response1 = createAuditorium(restTemplate, url, auditorium1Name, 25, true, buildingId);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        Auditorium auditorium = response1.getBody();
        int id = auditorium.getId();
        auditorium.setName("1-303А");
        auditorium.setCapacity(24);
        auditorium.setActive(false);
        ResponseEntity<Auditorium> response2 = restTemplate.exchange(url + "auditoriums" + "/" + id, HttpMethod.PUT,
                new HttpEntity<>(auditorium), Auditorium.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), notNullValue());

        auditorium.setId(id);
        assertThat(response2.getBody(), is(auditorium));
    }

    @Test
    public void testDeleteAuditorium() {
        int buildingId = createBuilding(restTemplate, url, "1").getBody().getId();

        String auditorium1Name = "1-301";
        ResponseEntity<Auditorium> response1 = createAuditorium(restTemplate, url, auditorium1Name, 25, true, buildingId);
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        int id = response1.getBody().getId();
        ResponseEntity<Void> response2 = restTemplate.exchange(url + "auditoriums" + "/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), nullValue());

        ResponseEntity<Auditorium> response3 = restTemplate.getForEntity(url + "auditoriums" +"/" + id, Auditorium.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
}
