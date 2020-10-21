package com.server.parser.java.task.verifier;

import com.google.common.base.Verify;
import com.server.parser.java.JavaParserAdapter;
import com.server.parser.java.ast.MethodPrintable;
import com.server.parser.java.ast.Statement;
import com.server.parser.java.ast.TaskAst;
import com.server.parser.java.task.model.StatementModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StatementVerifier {
    private final List<Statement> statements;
    private List<Statement> availableStatements;

    public StatementVerifier(TaskAst taskAst) {
        this.statements = taskAst.getClassAst().getBody().getMethods().stream()
                .flatMap(method -> method.getBody().getStatements().stream())
                .collect(Collectors.toList());
    }

    public void verify(StatementModel statementModel) {
        availableStatements = new ArrayList<>(statements);

        statementModel.getInMethod().ifPresent(this::verifyMethodName);
        statementModel.getText().ifPresent(this::verifyText);
        statementModel.getResolved().ifPresent(this::verifyResolved);
        Verify.verify(!availableStatements.isEmpty(), getErrorMessage(statementModel));
    }

    private String getErrorMessage(StatementModel statementModel) {
        StringBuilder builder = new StringBuilder();
        statementModel.getLogInfo().ifPresent(message -> builder.append(" \"").append(message).append('"'));
        return String.format("Oczekiwana instrukcja%s nie istnieje", builder);
    }

    private void verifyResolved(String resolved) {
        availableStatements = availableStatements.stream()
                .filter(statement -> areStatementsEqual(resolved, statement.getResolved()))
                .collect(Collectors.toList());
    }

    private boolean areStatementsEqual(String stmt1, String stmt2) {
        String stmt1Parsed = JavaParserAdapter.parseStatement(stmt1).getText();
        String stmt2Parsed = JavaParserAdapter.parseStatement(stmt2).getText();
        return stmt1Parsed.equals(stmt2Parsed);
    }

    private void verifyText(String text) {
        availableStatements = availableStatements.stream()
                .filter(statement -> areStatementsEqual(text, statement.getText()))
                .collect(Collectors.toList());
    }

    private void verifyMethodName(String methodName) {
        availableStatements = availableStatements.stream()
                .filter(statement -> statement instanceof MethodPrintable &&
                        ((MethodPrintable) statement).printMethodName().equals(methodName))
                .collect(Collectors.toList());
    }
}