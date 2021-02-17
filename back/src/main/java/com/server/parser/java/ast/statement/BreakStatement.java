package com.server.parser.java.ast.statement;

public class BreakStatement extends ExpressionStatement {
    public static final BreakStatement INSTANCE = new BreakStatement();

    private BreakStatement() {
        super("break");
    }

    @Override
    public boolean hasBreak() {
        return true;
    }

    @Override
    public String getResolved() {
        return "break";
    }
}