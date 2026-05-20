package com.example.rest.exception;

import java.util.Date;

public record ExceptionResponse(Date timestamp, String message, String description) {
}
