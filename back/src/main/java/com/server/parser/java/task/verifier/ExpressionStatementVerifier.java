package com.server.parser.java.task.verifier;

import com.google.common.base.Verify;
import com.server.parser.java.JavaParserAdapter;
import com.server.parser.java.ast.Task;
import com.server.parser.java.ast.statement.expression_statement.ExpressionStatement;
import com.server.parser.java.ast.statement.expression_statement.MethodPrintable;
import com.server.parser.java.ast.statement.property.StatementProperties;
import com.server.parser.java.task.model.StatementModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExpressionStatementVerifier {
    private final List<ExpressionStatement> statements;
    private List<ExpressionStatement> availableStatements;

    public ExpressionStatementVerifier(Task task) {
        this.statements = task.getCalledStatements().stream()
                .flatMap(statement -> statement.getExpressionStatements().stream())
                .collect(Collectors.toList());
    }

    public void verify(StatementModel statementModel) {
        availableStatements = new ArrayList<>(statements);

        statementModel.getInMethod().ifPresent(this::verifyMethodName);
        statementModel.getText().ifPresent(this::verifyText);
        statementModel.getResolved().ifPresent(this::verifyResolved);
        statementModel.getIfCond().ifPresent(this::verifyIfCond);
        statementModel.isInElse().ifPresent(this::verifyIsInElse);
        statementModel.getElseIfCond().ifPresent(this::verifyElseIfCond);
        statementModel.getSwitchExpr().ifPresent(this::verifySwitchExpr);
        statementModel.getSwitchLabel().ifPresent(this::verifySwitchLabel);
        statementModel.getForIteration().ifPresent(this::verifyForIteration);
        statementModel.getWhileIteration().ifPresent(this::verifyWhileIteration);
        statementModel.getDoWhileIteration().ifPresent(this::verifyDoWhileIteration);
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
        String stmt1Parsed = JavaParserAdapter.parseStatement(stmt1 + ';').getText();
        String stmt2Parsed = JavaParserAdapter.parseStatement(stmt2 + ';').getText();
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

    private void verifyIfCond(String ifCond) {
        String ifCondParsed = JavaParserAdapter.parseExpression(ifCond).getText();
        availableStatements = availableStatements.stream()
                .filter(statement -> isPropertyEqual(statement.getProperty(StatementProperties.IF_CONDITION), ifCondParsed))
                .filter(statement -> statement.getProperty(StatementProperties.IN_ELSE) == null)
                .collect(Collectors.toList());
    }

    private boolean isPropertyEqual(String property, String ifCondParsed) {
        if (property == null) {
            return false;
        }
        return property.equals(ifCondParsed);
    }

    private void verifyIsInElse(boolean isInElse) {
        availableStatements = availableStatements.stream()
                .filter(statement -> statement.getProperty(StatementProperties.IF_CONDITION) == null)
                .filter(statement -> isPropertyEqual(statement.getProperty(StatementProperties.IN_ELSE), String.valueOf(isInElse)))
                .collect(Collectors.toList());
    }

    private void verifyElseIfCond(String elseIfCond) {
        String ifCondParsed = JavaParserAdapter.parseExpression(elseIfCond).getText();
        availableStatements = availableStatements.stream()
                .filter(statement -> isPropertyEqual(statement.getProperty(StatementProperties.IF_CONDITION), ifCondParsed))
                .filter(statement -> isPropertyEqual(statement.getProperty(StatementProperties.IN_ELSE), String.valueOf(true)))
                .collect(Collectors.toList());
    }

    private void verifySwitchExpr(String switchExpr) {
        String switchExprParsed = JavaParserAdapter.parseExpression(switchExpr).getText();
        availableStatements = availableStatements.stream()
                .filter(statement -> isPropertyEqual(statement.getProperty(StatementProperties.SWITCH_EXPRESSION), switchExprParsed))
                .collect(Collectors.toList());
    }


    private void verifySwitchLabel(String switchLabel) {
        String switchLabelParsed = switchLabel.equals("default") ? "default" : JavaParserAdapter.parseExpression(switchLabel).getText();
        availableStatements = availableStatements.stream()
                .filter(statement -> isPropertyInJoined(statement.getProperty(StatementProperties.SWITCH_LABELS), switchLabelParsed))
                .collect(Collectors.toList());
    }

    private void verifyForIteration(Integer forIteration) {
        availableStatements = availableStatements.stream()
                .filter(statement -> isPropertyEqual(statement.getProperty(StatementProperties.FOR_ITERATION),
                        String.valueOf(forIteration)))
                .collect(Collectors.toList());
    }

    private void verifyWhileIteration(Integer whileIteration) {
        availableStatements = availableStatements.stream()
                .filter(statement -> isPropertyEqual(statement.getProperty(StatementProperties.WHILE_ITERATION),
                        String.valueOf(whileIteration)))
                .collect(Collectors.toList());
    }

    private void verifyDoWhileIteration(Integer doWhileIteration) {
        availableStatements = availableStatements.stream()
                .filter(statement -> isPropertyEqual(statement.getProperty(StatementProperties.DO_WHILE_ITERATION),
                        String.valueOf(doWhileIteration)))
                .collect(Collectors.toList());
    }

    private boolean isPropertyInJoined(String joinedProperty, String switchLabelParsed) {
        if (joinedProperty == null) {
            return false;
        }
        String[] properties = joinedProperty.split(",");
        for (String property : properties) {
            if (property.equals(switchLabelParsed)) {
                return true;
            }
        }
        return false;
    }
}