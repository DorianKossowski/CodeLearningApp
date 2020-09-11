package com.server.parser.java.task;

import com.server.parser.java.ast.Exercise;

import java.util.Objects;

public class TaskVerifier {
    private final MethodVerifier methodVerifier;

    public TaskVerifier(Exercise exercise) {
        this.methodVerifier = new MethodVerifier(Objects.requireNonNull(exercise, "exercise cannot be null"));
    }

    public void verifyMethod(MethodModel methodModel) {
        methodVerifier.verify(methodModel);
    }
}