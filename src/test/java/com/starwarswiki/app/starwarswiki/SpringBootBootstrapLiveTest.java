package com.starwarswiki.app.starwarswiki;

import com.starwarswiki.app.domain.StarWarsTopics;
import com.starwarswiki.app.secretsprovider.SWAPISecretsProvider;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {StarwarswikiApplication.class}, webEnvironment
        = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Log4j2
public class SpringBootBootstrapLiveTest {

    public static final String API_BASE_URL = "https://swapi.co/api";

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
        System.out.println("Response: " + response);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void testInvalidPerson() {
        String apiUrl = buildApiUrl("people", "John Wick");
        System.out.println("Hitting " + apiUrl);
        Response response = RestAssured.get(apiUrl);
        System.out.println("Response: " + response);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

}
