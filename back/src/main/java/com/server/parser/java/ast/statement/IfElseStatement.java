package com.server.parser.java.ast.statement;

import com.server.parser.java.ast.statement.expression_statement.ExpressionStatement;
import com.server.parser.java.ast.statement.property.StatementProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IfElseStatement extends Statement {
    private final Statement contentStatement;

    private IfElseStatement(Statement contentStatement) {
        super("IF ELSE Statement");
        this.contentStatement = contentStatement;
    }

    @Override
    public List<ExpressionStatement> getExpressionStatements() {
        List<ExpressionStatement> expressionStatements = new ArrayList<>();
        Optional.ofNullable(contentStatement).ifPresent(s -> expressionStatements.addAll(s.getExpressionStatements()));
        return expressionStatements;
    }

    @Override
    public void addProperty(String key, String value) {
        Optional.ofNullable(contentStatement).ifPresent(s -> s.addProperty(key, value));
    }

    public static IfElseStatement createIf(String condition, Statement contentStatement) {
        Optional.ofNullable(contentStatement).ifPresent(s -> addIfStatementProperty(s, StatementProperties.IF_CONDITION, condition));
        return new IfElseStatement(contentStatement);
    }

    private static void addIfStatementProperty(Statement statement, String propKey, String propValue) {
        statement.getExpressionStatements().forEach(exprStatement -> exprStatement.addProperty(propKey, propValue));
    }

    public static IfElseStatement createElse(Statement contentStatement) {
        Optional.ofNullable(contentStatement).ifPresent(s -> addIfStatementProperty(s, StatementProperties.IN_ELSE, "true"));
        return new IfElseStatement(contentStatement);
    }
}