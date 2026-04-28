package com.example.appointment.exceptions;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalHandleException {

    @ExceptionHandler(ExceptionConflict.class)
    public ResponseEntity<StandardError> handleConflict(ExceptionConflict e, HttpServletRequest request) {
         StandardError error = new StandardError(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "Error de conflito de dados",
                e.getMessage(),
                request.getRequestURI()
        );
         return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(NotFoundExceptionT.class)
    public ResponseEntity<StandardError> handleNotFound(NotFoundExceptionT e,HttpServletRequest request) {
        StandardError error = new StandardError(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Não encontrado, revise os dados inseridos",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }



}
