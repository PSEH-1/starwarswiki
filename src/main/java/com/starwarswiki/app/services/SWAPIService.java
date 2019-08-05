package com.starwarswiki.app.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service("swapiService")
public class SWAPIService implements InformationProvider {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SWAPIUrlBuilder urlBuilder;

    @Override
    public String fetch(String type, String name) {
        System.out.println("Received request to find information on: type = " + type + ", name = " + name);
        LOGGER.info("Received request to find information on: type: {}, name: {}", type, name);
        String apiResult = "";
        ResponseEntity<String> response = fetchAPIResponse(type, name);
        apiResult = parseTopicJSON(name, apiResult, response);
        System.out.println("Response from SWAPI: " + apiResult);
        LOGGER.info("Response from SWAPI: {}", apiResult);

        return apiResult;
    }

    private String parseTopicJSON(String name, String apiResult, ResponseEntity<String> response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode results = root.path("results");
            for (int i = 0; i < results.size(); i++) {
                JsonNode result = results.get(i);
                if (result.path("name").asText().equalsIgnoreCase(name)) {
                    apiResult = result.asText();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Encountered an exception while querying for the Star Wars topic! " + e.getMessage());
            LOGGER.error("Encountered an exception while querying for the Star Wars topic! " + e.getMessage());
        }

        return apiResult;
    }

    @Retryable(
            value = {RestClientException.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 5000))
    private ResponseEntity<String> fetchAPIResponse(String type, String name) {
        RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
        String apiUrl = urlBuilder.buildApiUrl(type, name);
        return restTemplate.getForEntity(apiUrl, String.class);
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 5000;
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeout);
        return clientHttpRequestFactory;
    }

}
