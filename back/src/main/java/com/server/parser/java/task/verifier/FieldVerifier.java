package com.server.parser.java.task.verifier;

import com.google.common.base.Verify;
import com.server.parser.java.ast.Task;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.task.model.FieldModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FieldVerifier extends CommonVerifier {
    private final List<VariableDef> fields;
    private List<VariableDef> availableFields;

    public FieldVerifier(Task task) {
        this.fields = Objects.requireNonNull(task, "task cannot be null").getClassAst().getBody().getFields();
    }

    public void verify(FieldModel fieldModel) {
        availableFields = new ArrayList<>(fields);
        fieldModel.getModifiers().ifPresent(this::verifyFieldModifiers);
        fieldModel.getType().ifPresent(this::verifyFieldType);
        fieldModel.getName().ifPresent(this::verifyFieldName);
        fieldModel.getValue().ifPresent(this::verifyFieldValue);
        Verify.verify(!availableFields.isEmpty(), getErrorMessage(fieldModel));
    }

    protected String getErrorMessage(FieldModel fieldModel) {
        StringBuilder builder = new StringBuilder();
        fieldModel.getName().ifPresent(name -> builder.append(" \"").append(name).append('"'));
        return String.format("Oczekiwane pole%s nie istnieje", builder);
    }

    private void verifyFieldType(String type) {
        availableFields = availableFields.stream()
                .filter(field -> field.getType().equals(type))
                .collect(Collectors.toList());
    }

    private void verifyFieldModifiers(List<String> modifiers) {
        availableFields = availableFields.stream()
                .filter(field -> hasSameModifiers(field.getModifiers(), modifiers))
                .collect(Collectors.toList());
    }

    private void verifyFieldName(String name) {
        availableFields = availableFields.stream()
                .filter(field -> field.getName().equals(name))
                .collect(Collectors.toList());
    }

    private void verifyFieldValue(String value) {
        availableFields = availableFields.stream()
                .filter(field -> field.getValue().getExpression().getResolvedText().equals(value))
                .collect(Collectors.toList());
    }
}