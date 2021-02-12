package com.server.parser.java.ast.statement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO: make more generic Call class
public class CallStatement extends Statement {
    private final CallInvocation callInvocation;
    private final List<Statement> contentStatements;

    public CallStatement(CallInvocation callInvocation, List<Statement> contentStatements) {
        super("CALL Statement");
        this.callInvocation = Objects.requireNonNull(callInvocation, "callInvocation cannot be null");
        this.contentStatements = Objects.requireNonNull(contentStatements, "contentStatements cannot be null");
    }

    public CallInvocation getCallInvocation() {
        return callInvocation;
    }

    @Override
    public List<ExpressionStatement> getExpressionStatements() {
        List<ExpressionStatement> expressionStatements = new ArrayList<>();
        expressionStatements.add(callInvocation);
        contentStatements.forEach(statement -> expressionStatements.addAll(statement.getExpressionStatements()));
        return expressionStatements;
    }
}