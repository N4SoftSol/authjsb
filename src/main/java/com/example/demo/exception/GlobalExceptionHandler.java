package com.example.demo.exception;

import com.example.demo.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            RefreshTokenNotFoundException.class)
    public ResponseEntity<ErrorResponse>
    handleRefreshTokenNotFound(
            RefreshTokenNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorResponse(
                                LocalDateTime.now(),
                                404,
                                "NOT_FOUND",
                                ex.getMessage(),
                                request.getRequestURI()));
    }

    @ExceptionHandler(
            RefreshTokenRevokedException.class)
    public ResponseEntity<ErrorResponse>
    handleRefreshTokenRevoked(
            RefreshTokenRevokedException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        new ErrorResponse(
                                LocalDateTime.now(),
                                401,
                                "UNAUTHORIZED",
                                ex.getMessage(),
                                request.getRequestURI()));
    }

    @ExceptionHandler(
            RefreshTokenExpiredException.class)
    public ResponseEntity<ErrorResponse>
    handleRefreshTokenExpired(
            RefreshTokenExpiredException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        new ErrorResponse(
                                LocalDateTime.now(),
                                401,
                                "UNAUTHORIZED",
                                ex.getMessage(),
                                request.getRequestURI()));
    }

    @ExceptionHandler(
            IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse>
    handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .badRequest()
                .body(
                        new ErrorResponse(
                                LocalDateTime.now(),
                                400,
                                "BAD_REQUEST",
                                ex.getMessage(),
                                request.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse>
    handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                LocalDateTime.now(),
                                500,
                                "INTERNAL_SERVER_ERROR",
                                ex.getMessage(),
                                request.getRequestURI()));
    }
}
