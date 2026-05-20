package com.example.rest.model;

public record Greeting(long id, String content) {

    private static final String TEMPLATE = "Hello, %s!";

    // (Factory Method) para construir a mensagem formatada
    public static Greeting sendGreeting(long id, String name) {
        return new Greeting(id, String.format(TEMPLATE, name));
    }
}
