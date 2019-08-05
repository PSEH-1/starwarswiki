package com.starwarswiki.app.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service("swapiService")
@Log4j2
public class SWAPIService implements InformationProvider {

    private final Logger LOG = LogManager.getLogger(SWAPIService.class);

    @Autowired
    private SWAPIUrlBuilder urlBuilder;

    @Override
    public String fetch(String type, String name) {
        System.out.println("Received request to find information on: type = " + type + ", name = " + name);
        String apiResult = "";
        ResponseEntity<String> response = fetchAPIResponse(type, name);
        apiResult = parseTopicJSON(name, apiResult, response);
        System.out.println("Response from SWAPI: " + apiResult);

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
        }

        return apiResult;
    }

    private ResponseEntity<String> fetchAPIResponse(String type, String name) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = urlBuilder.buildApiUrl(type, name);
        return restTemplate.getForEntity(apiUrl, String.class);
    }

}
