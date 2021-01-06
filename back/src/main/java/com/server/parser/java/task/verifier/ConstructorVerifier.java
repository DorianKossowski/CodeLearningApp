package com.server.parser.java.task.verifier;

import com.server.parser.java.ast.Task;
import com.server.parser.java.task.model.MethodModel;

import java.util.Objects;

public class ConstructorVerifier extends MethodVerifier {

    public ConstructorVerifier(Task task) {
        super(Objects.requireNonNull(task, "task cannot be null").getClassAst().getBody().getConstructors());
    }

    @Override
    protected String getErrorMessage(MethodModel methodModel) {
        StringBuilder builder = new StringBuilder();
        methodModel.getName().ifPresent(name -> builder.append(" \"").append(name).append('"'));
        return String.format("Oczekiwany konstruktor%s nie istnieje", builder);
    }
}