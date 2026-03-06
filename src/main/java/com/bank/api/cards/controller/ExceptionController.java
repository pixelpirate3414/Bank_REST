package com.bank.api.cards.controller;

import com.bank.api.cards.exception.CardNotFoundException;
import com.bank.api.cards.exception.CardOperationException;
import com.bank.api.cards.dto.response.ExceptionResponse;
import com.bank.api.cards.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionController {
    
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponse> handleResponseStatusException(
        ResponseStatusException e,
        HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(
            new ExceptionResponse(
                e.getStatusCode().value(),
                e.getReason(),
                e.getMessage(),
                request.getRequestURI()
            )
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(
        UserNotFoundException e,
        HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(
            new ExceptionResponse(
                404,
                "User not found",
                e.getMessage(),
                request.getRequestURI()
            )
        );
    }

    @ExceptionHandler(CardNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleCardNotFoundException(
        UserNotFoundException e,
        HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(
            new ExceptionResponse(
                404,
                "Card not found",
                e.getMessage(),
                request.getRequestURI()
            )
        );
    }

    @ExceptionHandler(CardOperationException.class)
    public ResponseEntity<ExceptionResponse> handleCardOperationException(
        CardOperationException e,
        HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(
            new ExceptionResponse(
                e.getStatusCode().value(),
                e.getReason(),
                e.getMessage(),
                request.getRequestURI()
            )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleServerException(
        Exception e,
        HttpServletRequest request
    ) {
        return ResponseEntity.badRequest().body(
            new ExceptionResponse(
                500,
                "Internal server error",
                e.getMessage(),
                request.getRequestURI()
            )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidationError(
        MethodArgumentNotValidException e,
        HttpServletRequest request
    ) {
        String message = e.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(
            new ExceptionResponse(
                400,
                "Validation error",
                message,
                request.getRequestURI()
            )
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolation(
        ConstraintViolationException e,
        HttpServletRequest request
    ) {
        String message = e.getConstraintViolations().stream()
            .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
            .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(
            new ExceptionResponse(
                400,
                "Constraint violation",
                message,
                request.getRequestURI()
            )
        );
    }
}
