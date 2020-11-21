package com.server.parser.java.ast.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IfElseStatement extends Statement {
    private final List<Statement> contentStatements;

    private IfElseStatement(List<Statement> contentStatements) {
        super("IF ELSE Statement");
        this.contentStatements = Objects.requireNonNull(contentStatements, "contentStatements cannot be null");
    }

    @Override
    public List<ExpressionStatement> getExpressionStatements() {
        List<ExpressionStatement> expressionStatements = new ArrayList<>();
        contentStatements.forEach(statement -> expressionStatements.addAll(statement.getExpressionStatements()));
        return expressionStatements;
    }

    @Override
    public void addProperty(String key, String value) {
        contentStatements.forEach(statement -> statement.addProperty(key, value));
    }

    public static IfElseStatement createIf(String condition, List<Statement> contentStatements) {
        contentStatements.forEach(statement -> addIfStatementProperty(statement, StatementProperties.IF_CONDITION, condition));
        return new IfElseStatement(contentStatements);
    }

    private static void addIfStatementProperty(Statement statement, String propKey, String propValue) {
        statement.getExpressionStatements().forEach(exprStatement -> exprStatement.addProperty(propKey, propValue));
    }

    public static IfElseStatement createElse(List<Statement> contentStatements) {
        contentStatements.forEach(statement -> addIfStatementProperty(statement, StatementProperties.IN_ELSE, "true"));
        return new IfElseStatement(contentStatements);
    }
}