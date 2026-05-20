package com.example.rest.controllers;

import com.example.rest.model.Greeting;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
@Profile("dev")
public class GreetingController {


    // private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    // http://localhost:8080/hello?name=Luis
    @RequestMapping("/hello")
    public Greeting greeting(
            @RequestParam(value="name", defaultValue = "World")
            String name){
        // return new Greeting(counter.incrementAndGet(),String.format(template, name)); //  return new Greeting(1, "Hello World");
        return Greeting.sendGreeting(counter.incrementAndGet(), name);
    }
}
