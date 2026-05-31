package com.example.rest.services;

public interface EmailService {
    void sendVerificationEmail(String toEmail, String firstName, String token);
}
