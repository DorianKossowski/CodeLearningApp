package com.server.parser.java.ast.statement;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.call.CallInvocation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// TODO: make more generic Call class
public class Call extends ExpressionStatement implements MethodPrintable {
    private final String contextMethodName;
    private final String name;
    private final List<Expression> args;

    public Call(String text, String contextMethodName, String name, List<Expression> args) {
        super(text);
        this.contextMethodName = Objects.requireNonNull(contextMethodName, "contextMethodName cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.args = Objects.requireNonNull(args, "args cannot be null");
    }

    public Call(CallInvocation invocation) {
        this(invocation.getText(), invocation.getContextMethodName(), invocation.getName(), invocation.getArgs());
    }

    public String getName() {
        return name;
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
        return name + "(" + resolvedArgs + ")";
    }
}