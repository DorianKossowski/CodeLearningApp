package com.server.parser.java.ast.statement;

import com.server.parser.java.ast.statement.expression_statement.CallInvocation;

import java.util.Collections;

public class PrintCallStatement extends CallStatement {

    public PrintCallStatement(CallInvocation callInvocation) {
        super(callInvocation, Collections.emptyList());
    }
}