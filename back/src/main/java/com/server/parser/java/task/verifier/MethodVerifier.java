package com.server.parser.java.task.verifier;

import com.google.common.base.Verify;
import com.server.parser.java.ast.Exercise;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.task.MethodModel;
import com.server.parser.java.task.ast.MethodArgs;

import java.util.ArrayList;
import java.util.Iterator;
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
        if (!methodModel.getArgs().isEmpty()) {
            verifyMethodArgs(methodModel.getArgs());
        }

        Verify.verify(!availableMethods.isEmpty(), String.format("Oczekiwana metoda: %s nie istnieje", methodModel));
    }

    private void verifyMethodName(String name) {
        availableMethods = availableMethods.stream()
                .filter(method -> method.getHeader().getName().equals(name))
                .collect(Collectors.toList());
    }

    private void verifyMethodArgs(List<MethodArgs> args) {
        availableMethods = availableMethods.stream()
                .filter(method -> hasSameMethodArgs(method.getHeader().getArguments(), args))
                .collect(Collectors.toList());
    }

    static boolean hasSameMethodArgs(List<Variable> actualArgs, List<MethodArgs> expectedArgs) {
        if (actualArgs.size() != expectedArgs.size()) {
            return false;
        }
        Iterator<Variable> actualArgsIt = actualArgs.iterator();
        Iterator<MethodArgs> expectedArgsIt = expectedArgs.iterator();

        while (actualArgsIt.hasNext()) {
            Variable actualArg = actualArgsIt.next();
            MethodArgs expectedArg = expectedArgsIt.next();

            boolean typeCompResult = expectedArg.getType().map(type -> actualArg.getType().equals(type)).orElse(true);
            boolean nameCompResult = expectedArg.getName().map(name -> actualArg.getName().equals(name)).orElse(true);
            if (!(typeCompResult && nameCompResult)) {
                return false;
            }
        }
        return true;
    }
}