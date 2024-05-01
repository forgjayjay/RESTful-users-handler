package com.assignment.testTask.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ApiExceptionHandler{
    
    public static final String DEFAULT_ERROR_VIEW = "error";
    
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<?> handleApiRequestException(ApiRequestException e){
        HttpStatusCode badRequest = HttpStatusCode.valueOf(400);
        ApiException apiException = new ApiException(e.getMessage(), badRequest, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<?> defaultErrorHandler(Exception e){
        HttpStatusCode badRequest = HttpStatusCode.valueOf(400);
        ApiException apiException = new ApiException("Wrong input type", badRequest, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = {InternalApiException.class})
    public ResponseEntity<?> handleInternalApiException(InternalApiException e){
        HttpStatusCode internalServerError = HttpStatusCode.valueOf(500);
        ApiException apiException = new ApiException(e.getMessage(), internalServerError, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, internalServerError);
    }

    @ExceptionHandler(value = {NoResourceFoundException.class})
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException e){
        HttpStatusCode noResourceFound = HttpStatusCode.valueOf(404);
        ApiException apiException = new ApiException("No resource found", noResourceFound, ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(apiException, noResourceFound);
    }

}
