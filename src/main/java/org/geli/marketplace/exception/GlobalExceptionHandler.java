package org.geli.marketplace.exception;

import org.geli.marketplace.util.ResponseUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ResponseUtil> buildResponse(HttpStatus status, String message) {
        ResponseUtil response = new ResponseUtil();
        response.setStatus(status.value());
        response.setMessage(message);
        return new ResponseEntity<>(response, status);
    }

    // --- 404 NOT FOUND ---
    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<ResponseUtil> handleNotFound(Exception ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Resource not found: " + ex.getMessage());
    }

    // --- 400 BAD REQUEST ---
    @ExceptionHandler({
            IllegalArgumentException.class, 
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class,
            DataIntegrityViolationException.class
    })
    public ResponseEntity<ResponseUtil> handleBadRequest(Exception ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid request: " + ex.getMessage());
    }

    // --- 409 CONFLICT (Concurrency) ---
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ResponseUtil> handleConcurrencyFailure(OptimisticLockingFailureException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Concurrency error: The record was updated by another transaction. Please try again.");
    }

    // --- 500 INTERNAL SERVER ERROR (Fallback) ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseUtil> handleGenericException(Exception ex) {
        ex.printStackTrace(); // Keep this for server logs during development
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage());
    }
}
