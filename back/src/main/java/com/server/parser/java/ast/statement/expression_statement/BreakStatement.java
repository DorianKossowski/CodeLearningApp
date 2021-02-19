package com.server.parser.java.ast.statement.expression_statement;

public class BreakStatement extends ExpressionStatement {
    public static final BreakStatement INSTANCE = new BreakStatement();

    private BreakStatement() {
        super("break");
    }

    @Override
    public String getResolved() {
        return "break";
    }
}