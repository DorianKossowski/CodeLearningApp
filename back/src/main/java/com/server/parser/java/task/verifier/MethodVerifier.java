package com.server.parser.java.task.verifier;

import com.google.common.base.Verify;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.Task;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.task.model.MethodArgs;
import com.server.parser.java.task.model.MethodModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodVerifier {
    private final List<Method> methods;
    private List<Method> availableMethods;

    public MethodVerifier(Task task) {
        this.methods = Objects.requireNonNull(task, "task cannot be null").getClassAst().getBody().getMethods();
    }

    protected MethodVerifier(List<Method> methods) {
        this.methods = Objects.requireNonNull(methods, "methods cannot be null");
    }

    public void verify(MethodModel methodModel) {
        availableMethods = new ArrayList<>(methods);
        if (!methodModel.getModifiers().isEmpty()) {
            verifyMethodModifiers(methodModel.getModifiers());
        }
        methodModel.getResult().ifPresent(this::verifyMethodResult);
        methodModel.getName().ifPresent(this::verifyMethodName);
        if (!methodModel.getArgs().isEmpty()) {
            verifyMethodArgs(methodModel.getArgs());
        }

        Verify.verify(!availableMethods.isEmpty(), getErrorMessage(methodModel));
    }

    protected String getErrorMessage(MethodModel methodModel) {
        StringBuilder builder = new StringBuilder();
        methodModel.getName().ifPresent(name -> builder.append(" \"").append(name).append('"'));
        return String.format("Oczekiwana metoda%s nie istnieje", builder);
    }

    private void verifyMethodResult(String result) {
        availableMethods = availableMethods.stream()
                .filter(method -> method.getHeader().getResult().equals(result))
                .collect(Collectors.toList());
    }

    private void verifyMethodModifiers(List<String> modifiers) {
        availableMethods = availableMethods.stream()
                .filter(method -> method.getHeader().getModifiers().containsAll(modifiers))
                .collect(Collectors.toList());
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

    static boolean hasSameMethodArgs(List<VariableDef> actualArgs, List<MethodArgs> expectedArgs) {
        if (actualArgs.size() != expectedArgs.size()) {
            return false;
        }
        Iterator<VariableDef> actualArgsIt = actualArgs.iterator();
        Iterator<MethodArgs> expectedArgsIt = expectedArgs.iterator();

        while (actualArgsIt.hasNext()) {
            VariableDef actualArg = actualArgsIt.next();
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