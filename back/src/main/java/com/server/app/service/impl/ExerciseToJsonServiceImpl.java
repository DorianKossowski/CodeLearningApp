package com.server.app.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.service.ExerciseToJsonService;
import com.server.parser.java.ast.Exercise;
import org.springframework.stereotype.Service;

@Service
public class ExerciseToJsonServiceImpl implements ExerciseToJsonService {

    @Override
    public String mapToJson(Exercise exercise) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(exercise);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Exception during mapping exercise to json", e);
        }
    }
}