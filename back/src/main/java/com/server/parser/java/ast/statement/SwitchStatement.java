package com.server.parser.java.ast.statement;

import java.util.Collections;
import java.util.List;

public class SwitchStatement extends Statement {

    public SwitchStatement() {
        super("SWITCH Statement");
    }

    @Override
    public List<ExpressionStatement> getExpressionStatements() {
        return Collections.emptyList();
    }
}