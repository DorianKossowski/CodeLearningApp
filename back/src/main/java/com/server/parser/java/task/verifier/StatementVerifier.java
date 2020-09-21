package com.server.parser.java.task.verifier;

import com.google.common.base.Verify;
import com.server.parser.java.ast.MethodStatement;
import com.server.parser.java.ast.TaskAst;
import com.server.parser.java.task.model.StatementModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatementVerifier {
    private final List<MethodStatement> statements;
    private List<MethodStatement> availableStatements;

    public StatementVerifier(TaskAst taskAst) {
        this.statements = taskAst.getClassAst().getBody().getMethods().stream()
                .flatMap(method -> method.getBody().getStatements().stream())
                .filter(statement -> statement instanceof MethodStatement)
                .map(statement -> ((MethodStatement) statement))
                .collect(Collectors.toList());
    }

    public void verify(StatementModel statementModel) {
        availableStatements = new ArrayList<>(statements);

        statementModel.getInMethod().ifPresent(this::verifyMethodName);
        statementModel.getText().ifPresent(this::verifyText);
        statementModel.getResolved().ifPresent(this::verifyResolved);
        Verify.verify(!availableStatements.isEmpty(), "Oczekiwana instrukcja nie istnieje");
    }

    private void verifyResolved(String resolved) {
        availableStatements = availableStatements.stream()
                .filter(statement -> statement.getResolved().equals(resolved))
                .collect(Collectors.toList());
    }

    private void verifyText(String text) {
        availableStatements = availableStatements.stream()
                .filter(statement -> statement.getText().equals(text))
                .collect(Collectors.toList());
    }

    private void verifyMethodName(String methodName) {
        availableStatements = availableStatements.stream()
                .filter(statement -> statement.getContextMethodName().equals(methodName))
                .collect(Collectors.toList());
    }
}