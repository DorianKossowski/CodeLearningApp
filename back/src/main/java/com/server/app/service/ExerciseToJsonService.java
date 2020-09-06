package com.server.app.service;

import com.server.parser.java.ast.Exercise;

public interface ExerciseToJsonService {
    String mapToJson(Exercise exercise);
}