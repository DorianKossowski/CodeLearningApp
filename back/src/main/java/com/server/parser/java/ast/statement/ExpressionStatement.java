package com.server.parser.java.ast.statement;

import java.util.Collections;
import java.util.List;

public abstract class ExpressionStatement extends Statement {

    protected ExpressionStatement(String text) {
        super(text);
    }

    @Override
    public List<ExpressionStatement> getExpressionStatements() {
        return Collections.singletonList(this);
    }
}