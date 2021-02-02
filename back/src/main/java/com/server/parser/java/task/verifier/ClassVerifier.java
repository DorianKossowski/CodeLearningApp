package com.server.parser.java.task.verifier;

import com.google.common.base.Verify;
import com.server.parser.java.ast.Task;
import com.server.parser.java.task.model.ClassModel;

import java.util.Objects;

public class ClassVerifier {
    private final String name;

    public ClassVerifier(Task task) {
        this.name = Objects.requireNonNull(task, "task cannot be null").getClassAst().getHeader().getName();
    }

    public void verify(ClassModel classModel) {
        classModel.getName().ifPresent(this::verifyMethodName);
    }

    private void verifyMethodName(String expectedName) {
        Verify.verify(name.equals(expectedName), String.format("Oczekiwana klasa %s nie istnieje", expectedName));
    }
}