package com.starwarswiki.app.controllers;

import com.starwarswiki.app.services.InformationProvider;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api")
public class StarWarsWikiController {

    private final Logger LOG = LogManager.getLogger(StarWarsWikiController.class);

    @Autowired
    @Qualifier("swapiService")
    private InformationProvider informationProvider;

    @RequestMapping(
            value = "/query",
            params = {"type", "name"},
            method = RequestMethod.GET)
    public String getBarBySimplePathWithExplicitRequestParams(
            @RequestParam("type") String type,
            @RequestParam("name") String name) {
        System.out.println("Received request to find information on: type = " + type + ", name = " + name);
        return informationProvider.fetch(type, name);
    }

}