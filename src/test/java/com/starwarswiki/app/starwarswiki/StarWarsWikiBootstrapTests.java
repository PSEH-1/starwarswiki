package com.starwarswiki.app.starwarswiki;

import com.starwarswiki.app.secretsprovider.SWAPISecretsProvider;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StarwarswikiApplication.class}, webEnvironment
        = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StarWarsWikiBootstrapTests {

    public static final String API_BASE_URL = "https://swapi.co/api";

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private String buildApiUrl(String type, String name) {
        String properNameArg = name;
        if (name.contains(" "))
            properNameArg = name.replaceAll(" ", "%20");
        return new StringBuffer(SWAPISecretsProvider.API_BASE_URL)
                .append("/")
                .append(type)
                .append("/?search=")
                .append(properNameArg)
                .toString();
    }

    @Test
    public void testValidPerson() {
        String apiUrl = buildApiUrl("people", "Luke Skywalker");
        System.out.println("Hitting " + apiUrl);
        Response response = RestAssured.get(apiUrl);
        System.out.println("Response: " + response.getBody().prettyPrint());
        LOGGER.info("Response: {}", response.getBody().prettyPrint());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void testInvalidPerson() {
        String apiUrl = buildApiUrl("people", "John Wick");
        System.out.println("Hitting " + apiUrl);
        Response response = RestAssured.get(apiUrl);
        System.out.println("Response: " + response.getBody().prettyPrint());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void testValidVehicle() {
        String apiUrl = buildApiUrl("vehicles", "Sand Crawler");
        System.out.println("Hitting " + apiUrl);
        Response response = RestAssured.get(apiUrl);
        System.out.println("Response: " + response.getBody().prettyPrint());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void testInvalidVehicle() {
        String apiUrl = buildApiUrl("vehicles", "Batmobile");
        System.out.println("Hitting " + apiUrl);
        Response response = RestAssured.get(apiUrl);
        System.out.println("Response: " + response.getBody().prettyPrint());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

}
