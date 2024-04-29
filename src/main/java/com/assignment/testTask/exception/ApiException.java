package com.assignment.testTask.exception;

import java.time.ZonedDateTime;

import org.springframework.http.HttpStatusCode;

public class ApiException {
    private final String message;
    private final HttpStatusCode httpStatusCode;
    private final ZonedDateTime timestamp;


    public ApiException(String message, HttpStatusCode httpStatusCode, ZonedDateTime timestamp) {
        this.message = message;
        this.httpStatusCode = httpStatusCode;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return this.message;
    }

    public HttpStatusCode getHttpStatus() {
        return this.httpStatusCode;
    }

    public ZonedDateTime getTimestamp() {
        return this.timestamp;
    }
}
