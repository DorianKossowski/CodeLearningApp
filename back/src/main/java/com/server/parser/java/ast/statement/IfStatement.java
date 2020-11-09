package com.server.parser.java.ast.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IfStatement extends Statement {
    private final List<Statement> contentStatements;

    private IfStatement(List<Statement> contentStatements) {
        super("IF Statement");
        this.contentStatements = Objects.requireNonNull(contentStatements, "contentStatements cannot be null");
    }

    @Override
    public List<ExpressionStatement> getExpressionStatements() {
        List<ExpressionStatement> expressionStatements = new ArrayList<>();
        contentStatements.forEach(statement -> expressionStatements.addAll(statement.getExpressionStatements()));
        return expressionStatements;
    }

    public static IfStatement createIf(String condition, List<Statement> contentStatements) {
        contentStatements.forEach(statement -> statement.addProperty(StatementProperties.IF_CONDITION, condition));
        return new IfStatement(contentStatements);
    }
}