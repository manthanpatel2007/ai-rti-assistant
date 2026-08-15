package com.hacthon.ai_rti_assistant.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.Path;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResourceNotFoundException> handleBadRequest(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestException> handleBadRequest(
            BadRequestException ex,
            HttpServletRequest request
    ) {


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex);
    }

//    @ExceptionHandler(ErrorResponse)
//    public ResponseEntity<ErrorResponse> handleGeneralException(
//            ErrorResponse ex,
//            HttpServletRequest request
//    ) {
//
//
//
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(ex);
//    }

    @ExceptionHandler(EmailallReady.class)
    public ResponseEntity<EmailallReady> handleBadRequest(
            EmailallReady ex,
            HttpServletRequest request
    ) {


        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex);
    }
}