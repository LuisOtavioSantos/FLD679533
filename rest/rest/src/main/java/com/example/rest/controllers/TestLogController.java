package com.example.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("dev")
public class TestLogController {


    private final Logger logger = LoggerFactory.getLogger(TestLogController.class);

    @GetMapping("/test")
    public String TestLog() {
        logger.debug("This is a debug message");
        logger.info("This is a info message");
        logger.warn("This is a warn message");
        logger.error("This is an error message");
        return "Logs Test";
    }

}
