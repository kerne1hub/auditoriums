package com.kernel.auditoriums.api;

import com.kernel.auditoriums.entity.Auditorium;
import com.kernel.auditoriums.repository.AuditoriumRepository;
import org.junit.Before;
import org.junit.Ignore;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuditoriumTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AuditoriumRepository repository;

    @LocalServerPort
    private int port;

    private String url;

    @Before
    public void resetDb() {
        repository.deleteAll();
        repository.flush();
        url = "http://localhost:" + port + "/api/auditoriums";
    }

    @Test
    public void testCreateAuditorium() {
        Auditorium auditorium = new Auditorium("1-303", 25, true);
        ResponseEntity<Auditorium> response = restTemplate.postForEntity(url, auditorium, Auditorium.class);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getName(), is(auditorium.getName()));
    }

    @Test
    public void testGetAuditoriums() {
        Auditorium auditorium1 = new Auditorium("1-303", 25, true);
        ResponseEntity<Auditorium> response1 = restTemplate.postForEntity(url, auditorium1, Auditorium.class);

        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        Auditorium auditorium2 = new Auditorium("6-407", 26, true);
        ResponseEntity<Auditorium> response2 = restTemplate.postForEntity(url, auditorium2, Auditorium.class);

        assertThat(response2.getStatusCode(), is(HttpStatus.CREATED));

        ResponseEntity<List<Auditorium>> response3 = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Auditorium>>() {
        });

        List<Auditorium> auditoriums = response3.getBody();

        assertThat(auditoriums, notNullValue());
        assertThat(auditoriums.size(), is(2));
        assertThat(auditoriums.get(0).getName(), is(auditorium1.getName()));
        assertThat(auditoriums.get(1).getName(), is(auditorium2.getName()));
    }

    @Test
    public void testGetAuditoriumDetails() {
        Auditorium auditorium1 = new Auditorium("1-303", 25, true);
        ResponseEntity<Auditorium> response1 = restTemplate.postForEntity(url, auditorium1, Auditorium.class);

        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        int id = response1.getBody().getId();
        ResponseEntity<Auditorium> response2 = restTemplate.getForEntity(url + "/" + id, Auditorium.class);

        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), notNullValue());
        assertThat(response2.getBody().getName(), is(auditorium1.getName()));
    }

    @Test
    public void testUpdateAuditoriumDetailsOk() {
        Auditorium auditorium1 = new Auditorium("1-303", 25, true);
        ResponseEntity<Auditorium> response1 = restTemplate.postForEntity(url, auditorium1, Auditorium.class);

        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        int id = response1.getBody().getId();
        auditorium1.setName("1-303А");
        auditorium1.setCapacity(24);
        auditorium1.setActive(false);
        ResponseEntity<Auditorium> response2 = restTemplate.exchange(url + "/" + id, HttpMethod.PUT,
                new HttpEntity<>(auditorium1), Auditorium.class);

        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), notNullValue());

        auditorium1.setId(id);
        assertThat(response2.getBody(), is(auditorium1));
    }

    @Test
    @Ignore
    public void testUpdateAuditoriumDetailsFail() {

    }

    @Test
    public void testDeleteAuditorium() {
        Auditorium auditorium1 = new Auditorium("1-303", 25, true);
        ResponseEntity<Auditorium> response1 = restTemplate.postForEntity(url, auditorium1, Auditorium.class);

        assertThat(response1.getStatusCode(), is(HttpStatus.CREATED));

        int id = response1.getBody().getId();
        ResponseEntity<Void> response2 = restTemplate.exchange(url + "/" + id, HttpMethod.DELETE, null, Void.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), nullValue());

        ResponseEntity<Auditorium> response3 = restTemplate.getForEntity(url + "/" + id, Auditorium.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.NOT_FOUND));
    }
}
