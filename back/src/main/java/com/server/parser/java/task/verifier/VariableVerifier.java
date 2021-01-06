package com.server.parser.java.task.verifier;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import com.server.parser.java.ast.Task;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.task.model.VariableModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VariableVerifier {
    private final List<VariableDef> variableDefs;
    private List<VariableDef> availableVariableDefs;

    public VariableVerifier(Task task) {
        this.variableDefs = task.getCalledStatements().stream()
                .filter(statement -> statement instanceof VariableDef)
                .map(statement -> ((VariableDef) statement))
                .collect(Collectors.toList());
    }

    public void verify(VariableModel variableModel) {
        availableVariableDefs = new ArrayList<>(variableDefs);

        variableModel.getText().ifPresent(this::verifyText);
        Verify.verify(!availableVariableDefs.isEmpty(), getErrorMessage(variableModel));
    }

    private String getErrorMessage(VariableModel variableModel) {
        StringBuilder builder = new StringBuilder();
        variableModel.getLogInfo().ifPresent(message -> builder.append(" \"").append(message).append('"'));
        return String.format("Oczekiwana instrukcja%s nie istnieje", builder);
    }

    private void verifyText(String text) {
        String[] typeWithName = text.split(" ");
        Preconditions.checkArgument(typeWithName.length == 2,
                "Number of elements in variable text should be 2 but is " + typeWithName.length);
        availableVariableDefs = availableVariableDefs.stream()
                .filter(variableDef -> typeWithName[0].equals(variableDef.getType()) && typeWithName[1].equals(variableDef.getName()))
                .collect(Collectors.toList());
    }
}