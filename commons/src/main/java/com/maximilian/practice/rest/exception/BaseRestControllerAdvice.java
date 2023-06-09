package com.maximilian.practice.rest.exception;

import com.maximilian.practice.rest.RestResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

public class BaseRestControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        RestResponse response = new RestResponse();
        response.setMessage(ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> "Field '" + e.getField() + "' " + e.getDefaultMessage())
                .collect(Collectors.joining(", ")));
        response.setSuccess(false);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(value
            = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConflict(
            ConstraintViolationException ex, WebRequest request) {

        RestResponse response = new RestResponse();
        response.setMessage(ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(". ")));
        response.setSuccess(false);

        return handleExceptionInternal(ex,
                response,
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE, request);
    }

    @ExceptionHandler(value
            = {Exception.class})
    protected ResponseEntity<Object> handleAllExceptions(
            Exception ex, WebRequest request) {

        RestResponse response = new RestResponse();
        response.setMessage(ex.getMessage());
        response.setSuccess(false);

        return handleExceptionInternal(ex,
                response,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value
            = {GeneralException.class})
    protected ResponseEntity<Object> handleGeneralException(
            GeneralException ex, WebRequest request) {

        RestResponse response = new RestResponse();
        response.setMessage(ex.getMessage());
        response.setSuccess(false);

        return handleExceptionInternal(ex,
                response,
                new HttpHeaders(),
                ex.getResponseStatus(), request);
    }

}
