package com.server.parser.java.ast.statement.expression_statement;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.call.CallReference;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CallInvocation extends ExpressionStatement implements MethodPrintable {
    private final String contextMethodName;
    private final CallReference callReference;
    private final List<Expression> args;

    public CallInvocation(String text, String contextMethodName, CallReference callReference, List<Expression> args) {
        super(text);
        this.contextMethodName = Objects.requireNonNull(contextMethodName, "contextMethodName cannot be null");
        this.callReference = Objects.requireNonNull(callReference, "callReference cannot be null");
        this.args = Objects.requireNonNull(args, "args cannot be null");
    }

    public CallReference getCallReference() {
        return callReference;
    }

    public String getName() {
        return callReference.getCallName();
    }

    public List<Expression> getArgs() {
        return args;
    }

    @Override
    public String printMethodName() {
        return contextMethodName;
    }

    @Override
    public String getResolved() {
        String resolvedArgs = args.stream().map(Expression::getResolvedText).collect(Collectors.joining(", "));
        return callReference.getCallName() + "(" + resolvedArgs + ")";
    }
}