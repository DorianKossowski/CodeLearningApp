package com.server.parser.java.call.executor;

import com.google.common.collect.Iterables;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.CallInvocation;
import com.server.parser.java.constant.BooleanConstant;
import com.server.parser.java.context.ContextFactory;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.value.Value;
import com.server.parser.java.visitor.StatementListVisitor;

import java.util.Collections;
import java.util.List;

public class MethodCallExecutor extends CallExecutor {

    public MethodCallExecutor() {
        this(new StatementListVisitor());
    }

    MethodCallExecutor(StatementListVisitor visitor) {
        super(visitor);
    }

    @Override
    public CallStatement execute(Method method, CallInvocation invocation) {
        ObjectValue thisValue = getThisValue(invocation);
        JavaContext executionContext = createExecutionContext(method, thisValue);
        List<Statement> statements = executeInContext(method, invocation, executionContext);
        Expression returnedExpression = getReturnedExpression(statements);
        validateReturnedExpression(method.getHeader().getResult(), returnedExpression);
        return new CallStatement(invocation, statements, returnedExpression);
    }

    JavaContext createExecutionContext(Method method, ObjectValue thisValue) {
        return ContextFactory.createExecutionContext(thisValue, method.getMethodContext());
    }

    private ObjectValue getThisValue(CallInvocation invocation) {
        return invocation.getCallReference().getValue()
                .orElseThrow(() -> new IllegalArgumentException("Should provide value"));
    }

    public CallStatement executeSpecialEqualsMethod(CallInvocation invocation) {
        Value thisValue = getThisValue(invocation);
        Expression argument = Iterables.getOnlyElement(invocation.getArgs());

        boolean areEqual = thisValue.equalsMethod(argument.getValue());
        Literal result = new Literal(new BooleanConstant(areEqual));
        return new CallStatement(invocation, Collections.emptyList(), result);
    }
}