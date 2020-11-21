package com.server.parser.java.ast.statement;

import java.util.Collections;
import java.util.List;

public class BreakStatement extends Statement {
    public static final BreakStatement INSTANCE = new BreakStatement();

    private BreakStatement() {
        super("BREAK");
    }

    @Override
    public List<ExpressionStatement> getExpressionStatements() {
        return Collections.emptyList();
    }
}