package com.server.app.service.json;

import com.server.parser.java.ast.Exercise;

public interface ExerciseToJsonService {
    String mapToJson(Exercise exercise);
}