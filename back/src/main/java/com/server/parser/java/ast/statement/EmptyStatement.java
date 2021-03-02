package com.server.parser.java.ast.statement;

import com.server.parser.java.ast.statement.expression_statement.ExpressionStatement;

import java.util.Collections;
import java.util.List;

public class EmptyStatement extends Statement {
    public static final EmptyStatement INSTANCE = new EmptyStatement();

    private EmptyStatement() {
        super(";");
    }

    @Override
    public List<ExpressionStatement> getExpressionStatements() {
        return Collections.emptyList();
    }
}