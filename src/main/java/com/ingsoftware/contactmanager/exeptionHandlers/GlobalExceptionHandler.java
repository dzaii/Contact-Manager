package com.ingsoftware.contactmanager.exeptionHandlers;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.directory.AttributeInUseException;
import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Date;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> customErrorHandling(Exception exception) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Unknown error.",
                exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> customValidation(MethodArgumentNotValidException exception) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Validation Error.",
                exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<?> entityNotFound(EntityNotFoundException exception) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Entity not found.",
                exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity<?> duplicateKey(DuplicateKeyException exception) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Duplicate key.",
                exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<?> accessDenied(AccessDeniedException exception) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Access denied.",
                exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AttributeInUseException.class})
    public ResponseEntity<?> attributeInUse(AttributeInUseException exception) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "Attribute in use.",
                exception.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
