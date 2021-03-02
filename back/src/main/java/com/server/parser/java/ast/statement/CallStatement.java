package com.server.parser.java.ast.statement;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.VoidExpression;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.ast.statement.expression_statement.ExpressionStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// TODO: make more generic Call class
public class CallStatement extends Statement {
    private final CallInvocation callInvocation;
    private final List<Statement> contentStatements;
    private final Expression result;

    public CallStatement(CallInvocation callInvocation, List<Statement> contentStatements) {
        this(callInvocation, contentStatements, VoidExpression.INSTANCE);
    }

    public CallStatement(CallInvocation callInvocation, List<Statement> contentStatements, Expression result) {
        super("CALL Statement");
        this.callInvocation = Objects.requireNonNull(callInvocation, "callInvocation cannot be null");
        this.contentStatements = Objects.requireNonNull(contentStatements, "contentStatements cannot be null");
        this.result = Objects.requireNonNull(result, "result cannot be null");
    }

    public CallInvocation getCallInvocation() {
        return callInvocation;
    }

    public Expression getResult() {
        return result;
    }

    @Override
    public List<ExpressionStatement> getExpressionStatements() {
        List<ExpressionStatement> expressionStatements = new ArrayList<>();
        expressionStatements.add(callInvocation);
        contentStatements.forEach(statement -> expressionStatements.addAll(statement.getExpressionStatements()));
        return expressionStatements;
    }
}