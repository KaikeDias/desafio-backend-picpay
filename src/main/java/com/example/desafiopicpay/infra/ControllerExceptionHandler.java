package com.example.desafiopicpay.infra;

import com.example.desafiopicpay.dtos.ExceptionDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity threatDuplicateEntry(DataIntegrityViolationException exception) {
        ExceptionDto exceptionDto = new ExceptionDto("Usuario já cadastrado!", "400");
        return ResponseEntity.badRequest().body(exceptionDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity threat404(DataIntegrityViolationException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity threatGeneralExceptions(Exception exception) {
        ExceptionDto exceptionDto = new ExceptionDto(exception.getMessage(), "500");
        return ResponseEntity.internalServerError().body(exceptionDto);
    }
}
