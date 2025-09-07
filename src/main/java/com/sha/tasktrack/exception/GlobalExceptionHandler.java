package com.sha.tasktrack.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req){
        ApiError body = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest req){
        ApiError body = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .build();
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req){
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField()+": "+f.getDefaultMessage())
                .findFirst().orElse("Validation error");
        ApiError body = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message(msg)
                .path(req.getRequestURI())
                .build();
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleOther(Exception ex, HttpServletRequest req){
        ApiError body = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Server Error")
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity<ApiError> handleAuthException(AuthenticationException ex, HttpServletRequest req){
        ApiError body = ApiError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("Unauthorized")
                .message("Invalid email or password")
                .path(req.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

}
