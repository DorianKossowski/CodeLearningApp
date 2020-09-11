package com.server.parser.java.task;

import com.google.common.base.Verify;
import com.server.parser.java.ast.Exercise;
import com.server.parser.java.ast.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodVerifier {
    private final List<Method> methods;
    private List<Method> availableMethods;

    public MethodVerifier(Exercise exercise) {
        this.methods = Objects.requireNonNull(exercise, "exercise cannot be null").getClassAst().getBody().getMethods();
    }

    public void verify(MethodModel methodModel) {
        availableMethods = new ArrayList<>(methods);
        methodModel.getName().ifPresent(this::verifyMethodName);

        Verify.verify(!availableMethods.isEmpty(), "Oczekiwana metoda nie istnieje");
    }

    private void verifyMethodName(String name) {
        availableMethods = availableMethods.stream()
                .filter(method -> method.getHeader().getName().equals(name))
                .collect(Collectors.toList());
    }
}