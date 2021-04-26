package com.server.parser.java.ast.statement;

import com.server.parser.java.ast.Statements;
import com.server.parser.java.ast.statement.expression_statement.ExpressionStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockStatement extends Statement {
    private final Statements statements;

    public BlockStatement(Statements statements) {
        super("{ BLOCK STATEMENT }");
        this.statements = Objects.requireNonNull(statements, "statements cannot be null");
    }

    public List<Statement> getStatements() {
        return statements.getStatements();
    }

    @Override
    public List<ExpressionStatement> getExpressionStatements() {
        List<ExpressionStatement> expressionStatements = new ArrayList<>();
        statements.getStatements().forEach(statement -> expressionStatements.addAll(statement.getExpressionStatements()));
        return expressionStatements;
    }
}