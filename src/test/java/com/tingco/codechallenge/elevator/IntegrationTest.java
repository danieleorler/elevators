package com.tingco.codechallenge.elevator;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;


/**
 * Boiler plate test class to get up and running with a test faster.
 *
 * @author Sven Wesley
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testPing() throws Exception {
        RequestEntity<Void> request = RequestEntity.get(new URI("/rest/v1/ping")).build();
        String response = restTemplate.exchange(request, String.class).getBody();
        assertEquals("pong", response);
    }

    @Test
    public void testRequestElevator() throws Exception {
        RequestEntity<Void> request = RequestEntity.get(new URI("/rest/v1/request/1")).build();
        String response = restTemplate.exchange(request, String.class).getBody();
        assertEquals("Elevator 1 is coming", response);
    }

    @Test
    @Ignore("not independent")
    public void testRequestTooManyElevators() throws Exception {

        RequestEntity<Void> request = RequestEntity.get(new URI("/rest/v1/request/10")).build();
        IntStream.range(0,6)
            .forEach(i -> {
                ResponseEntity<String> response = restTemplate.exchange(request, String.class);
                assertEquals(200, response.getStatusCodeValue());
            });

        ResponseEntity<String> responseEntity = restTemplate.exchange(request, String.class);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testMoveElevatorTwiceNotFound() throws Exception {
        RequestEntity<Void> request = RequestEntity.get(new URI("/rest/v1/elevator/1/move/10")).build();
        String response = restTemplate.exchange(request, String.class).getBody();
        assertEquals("Elevator 1 is moving to floor 10", response);

        request = RequestEntity.get(new URI("/rest/v1/elevator/1/move/3")).build();
        ResponseEntity<String> responseEntity = restTemplate.exchange(request, String.class);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testMoveElevatorTwice() throws Exception {
        RequestEntity<Void> request = RequestEntity.get(new URI("/rest/v1/elevator/0/move/0")).build();
        String response = restTemplate.exchange(request, String.class).getBody();
        assertEquals("Elevator 0 is moving to floor 0", response);
        Thread.sleep(200);
        request = RequestEntity.get(new URI("/rest/v1/elevator/0/move/1")).build();
        response = restTemplate.exchange(request, String.class).getBody();
        assertEquals("Elevator 0 is moving to floor 1", response);
    }

}
