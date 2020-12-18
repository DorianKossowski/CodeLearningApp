package com.server.parser.java.ast.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WhileStatement extends Statement {
    private final List<Statement> contentStatements;

    public WhileStatement(List<Statement> contentStatements) {
        super("WHILE Statement");
        this.contentStatements = Objects.requireNonNull(contentStatements, "contentStatements cannot be null");
    }

    @Override
    public List<ExpressionStatement> getExpressionStatements() {
        List<ExpressionStatement> expressionStatements = new ArrayList<>();
        contentStatements.forEach(statement -> expressionStatements.addAll(statement.getExpressionStatements()));
        return expressionStatements;
    }
}