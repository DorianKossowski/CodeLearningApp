package com.server.parser.java.ast.statement.expression_statement;

import com.server.parser.java.ast.statement.Statement;

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

    public abstract String getResolved();
}