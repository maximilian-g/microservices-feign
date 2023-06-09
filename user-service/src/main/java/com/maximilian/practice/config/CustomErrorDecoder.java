package com.maximilian.practice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maximilian.practice.rest.clients.CommonErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomErrorDecoder extends CommonErrorDecoder {

    private final ObjectMapper mapper;

    @Override
    protected ObjectMapper getMapper() {
        return mapper;
    }

}
