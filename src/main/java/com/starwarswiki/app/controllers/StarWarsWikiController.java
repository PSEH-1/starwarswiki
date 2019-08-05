package com.starwarswiki.app.controllers;

import com.starwarswiki.app.services.InformationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StarWarsWikiController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("swapiService")
    private InformationProvider informationProvider;

    @RequestMapping(
            value = "/query",
            method = RequestMethod.GET)
    String query(@RequestParam(value = "type") String type,
                 @RequestParam(value = "name") String name) {
        System.out.println("Received request to find information on: type = " + type + ", name = " + name);
        LOGGER.info("Received request to find information on: type: {}, name: {} ", type, name);
        return informationProvider.fetch(type, name);
    }

}