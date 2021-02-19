package com.server.parser.java.ast.statement.expression_statement;

public class BreakExprStatement extends ExpressionStatement {
    public static final BreakExprStatement INSTANCE = new BreakExprStatement();

    private BreakExprStatement() {
        super("break");
    }

    @Override
    public String getResolved() {
        return "break";
    }
}