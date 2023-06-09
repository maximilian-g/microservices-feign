package com.maximilian.practice.rest.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

public class GeneralException extends RuntimeException {

    @JsonIgnore
    protected final HttpStatus responseStatus;

    public GeneralException(String message) {
        super(message);
        responseStatus = HttpStatus.BAD_REQUEST;
    }

    public GeneralException(String message, HttpStatus status) {
        super(message);
        responseStatus = status;
    }

    public HttpStatus getResponseStatus() {
        return responseStatus;
    }

}
