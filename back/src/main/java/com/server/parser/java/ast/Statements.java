package com.server.parser.java.ast;

import com.server.parser.java.ast.statement.Statement;

import java.util.List;
import java.util.Objects;

public class Statements implements AstElement {
    private final List<Statement> statements;

    public Statements(List<Statement> statements) {
        this.statements = Objects.requireNonNull(statements, "statements cannot be null");
    }

    public List<Statement> getStatements() {
        return statements;
    }
}