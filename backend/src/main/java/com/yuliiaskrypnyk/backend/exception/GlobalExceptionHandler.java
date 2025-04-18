package com.yuliiaskrypnyk.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorMessage error = new ErrorMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error); // HTTP 404
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGeneralException(Exception e) {
        String userMessage = "An error occurred on the server. Please try again later.";
        ErrorMessage error = new ErrorMessage(userMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error); // HTTP 500
    }
}
