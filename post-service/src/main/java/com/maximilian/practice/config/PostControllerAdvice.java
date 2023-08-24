package com.maximilian.practice.config;

import com.maximilian.practice.rest.exception.BaseRestControllerAdvice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@Slf4j
public class PostControllerAdvice extends BaseRestControllerAdvice {

    @Override
    protected ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        log.error("Got unexpected exception, message: " + ex.getMessage(), ex);
        return super.handleAllExceptions(ex, request);
    }

}
