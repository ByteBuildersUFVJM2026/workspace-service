package com.LigaAcademic.AcademicProject.Exceptions;


import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardErrorDTO> handlerEntityNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardErrorDTO erro = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Não encontrado",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(erro);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardErrorDTO> handlerIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardErrorDTO erro = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Argumento inválido",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(erro);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<StandardErrorDTO> handlerConflictException(ConflictException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        StandardErrorDTO erro = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Conflito",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorDTO> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {

        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        String mensagem = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(field -> field.getField() + ": " + field.getDefaultMessage())
                .orElse("Dados inválidos");

        StandardErrorDTO erro = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Dados inválidos",
                mensagem,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(erro);
    }
}
