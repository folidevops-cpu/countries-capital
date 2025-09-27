package com.example.countrycapital;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CountryCapitalIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnIndexPageWithRandomPort() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/", String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Country Capital & Task Manager");
        assertThat(response.getBody()).contains("Welcome to Country Capital & Task Manager");
    }

    @Test
    void shouldReturnCapitalForUSA() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/capital?country=USA", String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Washington, D.C.");
    }

    @Test
    void shouldReturnCapitalForGermany() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/capital?country=Germany", String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Berlin");
    }

    @Test
    void shouldReturnUnknownForNonExistentCountry() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/capital?country=Atlantis", String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Unknown");
    }
}