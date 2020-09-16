package com.server.app.service.impl.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.service.json.TaskToJsonService;
import com.server.parser.java.ast.TaskAst;
import org.springframework.stereotype.Service;

@Service
public class TaskToJsonServiceImpl implements TaskToJsonService {

    @Override
    public String mapToJson(TaskAst taskAst) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(taskAst);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Exception during mapping task to json", e);
        }
    }
}