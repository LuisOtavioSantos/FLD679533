package com.example.rest.exception.handler;

import com.example.rest.exception.ExceptionResponse;
import com.example.rest.exception.ResourceNotFoundException;
import com.example.rest.exception.UnsupportedMathOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

// Antes era necessário duas Anotações para definir
//@ControllerAdvice
//@RestController
@RestControllerAdvice
public class CustomEntityResponseHandler extends ResponseEntityExceptionHandler {

    // Nossa "rede de segurança" para erros genéricos (Erro 500)
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR); // RFC 7807
    }

    // Tratamento para o erro matemático específico (Erro 400)
    @ExceptionHandler(UnsupportedMathOperationException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestExceptions(
            UnsupportedMathOperationException ex, 
            WebRequest request) 
    {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundExceptions(
            ResourceNotFoundException ex, // 4. Tipado com a exceção específica
            WebRequest request) //
    {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}