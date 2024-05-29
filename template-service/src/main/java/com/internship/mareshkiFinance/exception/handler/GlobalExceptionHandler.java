package com.internship.mareshkiFinance.exception.handler;



import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {


    private ResponseEntity<ErrorResponse> createErrorResponse(Throwable exception, HttpStatus status, String message) {

        var errorResponse = ErrorResponse.create(exception, status, message);
        return ResponseEntity.status(status).body(errorResponse);
    };

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {

        return createErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, "");
    }



}
