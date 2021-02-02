package com.server.parser.java.task.verifier;

import com.google.common.base.Verify;
import com.server.parser.java.ast.ClassHeader;
import com.server.parser.java.ast.Task;
import com.server.parser.java.task.model.ClassModel;

import java.util.List;
import java.util.Objects;

public class ClassVerifier extends CommonVerifier {
    private final ClassHeader classHeader;

    public ClassVerifier(Task task) {
        this.classHeader = Objects.requireNonNull(task, "task cannot be null").getClassAst().getHeader();
    }

    public void verify(ClassModel classModel) {
        classModel.getModifiers().ifPresent(this::verifyClassModifiers);
        classModel.getName().ifPresent(this::verifyMethodName);
    }

    private void verifyClassModifiers(List<String> modifiers) {
        List<String> actualModifiers = classHeader.getModifiers();
        Verify.verify(hasSameModifiers(actualModifiers, modifiers), "Oczekiwana klasa nie istnieje");
    }

    private void verifyMethodName(String expectedName) {
        Verify.verify(classHeader.getName().equals(expectedName), String.format("Oczekiwana klasa %s nie istnieje", expectedName));
    }
}