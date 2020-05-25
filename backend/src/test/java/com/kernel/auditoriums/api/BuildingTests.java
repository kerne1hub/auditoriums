package com.kernel.auditoriums.api;

import com.kernel.auditoriums.AuditoriumsApplication;
import com.kernel.auditoriums.entity.Building;
import com.kernel.auditoriums.entity.Lecturer;
import com.kernel.auditoriums.repository.BuildingRepository;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = AuditoriumsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BuildingTests extends TestBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @LocalServerPort
    protected int port;

    private String url;

    @Before
    public void setUp() {
        buildingRepository.deleteAll();
        buildingRepository.flush();
        lecturerRepository.deleteAll();
        lecturerRepository.flush();

        url = "http://localhost:" + port + "/api/buildings";

        Lecturer user = new Lecturer("User", "Test", null, "user@mail.cc", "login", "password", "Lecturer");
        registerAndAuth(passwordEncoder, user, restTemplate, port);
    }

    @Test
    public void createBuilding() {
        ResponseEntity<Building> response = createBuilding(restTemplate, url, "1");
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody(), notNullValue(Building.class));
    }

    @Test
    public void testGetBuildings() {
        ResponseEntity<Building> response1 = createBuilding(restTemplate, url, "1");
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        ResponseEntity<Building> response2 = createBuilding(restTemplate, url, "2");
        assertThat(response2.getStatusCode(), is(HttpStatus.CREATED));

        ResponseEntity<List<Building>> response3 = getEntityList(restTemplate, url, new ParameterizedTypeReference<List<Building>>() {});
        assertThat(response3.getBody(), notNullValue());
        assertThat(response3.getBody().size(), is(2));
        assertThat(response3.getBody().get(0), notNullValue(Building.class));
    }

    @Test
    public void testGetBuildingDetails() {
        ResponseEntity<Building> response1 = createBuilding(restTemplate, url, "1");
        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        int id = response1.getBody().getId();
        ResponseEntity<Building> response2 = getEntity(restTemplate, url, id, Building.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), notNullValue(Building.class));
    }

}
