package com.server.parser.java.call.executor;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.constant.BooleanConstant;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.visitor.StatementListVisitor;

import java.util.Collections;

public class MethodCallExecutor extends CallExecutor {

    public MethodCallExecutor() {
        this(new StatementListVisitor());
    }

    MethodCallExecutor(StatementListVisitor visitor) {
        super(visitor);
    }

    @Override
    public CallStatement execute(Method method, CallInvocation invocation) {
        return executeSpecialEqualsMethod(invocation);
    }


    public CallStatement executeSpecialEqualsMethod(CallInvocation invocation) {
        Value value = invocation.getCallReference().getValue()
                .orElseThrow(() -> new IllegalArgumentException("Should provide value"));
        Expression argument = Iterables.getOnlyElement(invocation.getArgs());

        boolean areEqual = value.equalsMethod(argument.getValue());
        Literal result = new Literal(new BooleanConstant(areEqual));
        return new CallStatement(invocation, Collections.emptyList(), result);
    }
}