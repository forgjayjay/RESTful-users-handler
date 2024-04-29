package com.assignment.testTask.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler{
    
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<?> handleApiRequestException(ApiRequestException e){
        HttpStatusCode badRequest = HttpStatusCode.valueOf(400);
        ApiException apiException = new ApiException(e.getMessage(), badRequest, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {InternalApiException.class})
    public ResponseEntity<?> handleInternalApiException(InternalApiException e){
        HttpStatusCode internalServerError = HttpStatusCode.valueOf(500);
        ApiException apiException = new ApiException(e.getMessage(), internalServerError, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, internalServerError);
    }
}
