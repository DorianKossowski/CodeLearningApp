package com.server.parser.java.task.verifier;

import com.google.common.base.Verify;
import com.server.parser.java.ast.ClassHeader;
import com.server.parser.java.ast.Task;
import com.server.parser.java.task.model.ClassModel;

import java.util.List;
import java.util.Objects;

public class ClassVerifier {
    private final ClassHeader classHeader;

    public ClassVerifier(Task task) {
        this.classHeader = Objects.requireNonNull(task, "task cannot be null").getClassAst().getHeader();
    }

    public void verify(ClassModel classModel) {
        classModel.getModifiers().ifPresent(this::verifyClassModifiers);
        classModel.getName().ifPresent(this::verifyMethodName);
    }

    // TODO make verifying modifiers common for all verifiers classes
    private void verifyClassModifiers(List<String> modifiers) {
        List<String> actualModifiers = classHeader.getModifiers();
        Verify.verify(hasSameModifiers(actualModifiers, modifiers), "Oczekiwana klasa nie istnieje");
    }

    private boolean hasSameModifiers(List<String> actualModifiers, List<String> modifiers) {
        if (modifiers.size() == 0) {
            return actualModifiers.isEmpty();
        }
        return actualModifiers.containsAll(modifiers);
    }

    private void verifyMethodName(String expectedName) {
        Verify.verify(classHeader.getName().equals(expectedName), String.format("Oczekiwana klasa %s nie istnieje", expectedName));
    }
}