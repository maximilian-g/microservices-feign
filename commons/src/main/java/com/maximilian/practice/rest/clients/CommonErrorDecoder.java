package com.maximilian.practice.rest.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maximilian.practice.rest.exception.GeneralException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;


@Slf4j
public abstract class CommonErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        log.info("Got exception for method key " + methodKey);
        if ((response.status() == HttpStatus.FORBIDDEN.value() ||
                response.status() == HttpStatus.UNAUTHORIZED.value()) &&
                response.body() == null) {
            return new GeneralException("Not authorized to perform operation '" +
                    response.request().httpMethod().name() +
                    "' for url '" + response.request().url() + "'.");
        }
        try (InputStream bodyIs = response.body().asInputStream()) {
            GeneralException message = getMapper().readValue(bodyIs, GeneralException.class);
            return new GeneralException("Error occurred, message: " + message.getMessage() +
                    ", initial status is " + message.getResponseStatus().name() + ".");
        } catch (IOException e) {
            return new GeneralException("Error during parsing response, message: " + e.getMessage());
        }
    }

    protected abstract ObjectMapper getMapper();

}
