package com.assignment.testTask.exception;

public class InternalApiException extends RuntimeException{
    public InternalApiException(String message){
        super(message);
    }

    public InternalApiException(String message, Throwable cause){
        super(message, cause);
    }
}
