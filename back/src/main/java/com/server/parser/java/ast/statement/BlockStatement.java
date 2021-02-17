package com.server.parser.java.ast.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockStatement extends Statement {
    private final List<Statement> statements;

    public BlockStatement(List<Statement> statements) {
        super("{ BLOCK STATEMENT }");
        this.statements = Objects.requireNonNull(statements, "statements cannot be null");
    }

    public List<Statement> getStatements() {
        return statements;
    }

    @Override
    public List<ExpressionStatement> getExpressionStatements() {
        List<ExpressionStatement> expressionStatements = new ArrayList<>();
        statements.forEach(statement -> expressionStatements.addAll(statement.getExpressionStatements()));
        return expressionStatements;
    }
}