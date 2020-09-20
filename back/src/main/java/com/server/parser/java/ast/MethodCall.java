package com.server.parser.java.ast;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodCall extends MethodStatement {
    private final String name;
    private final List<Expression> args;

    public MethodCall(String text, String contextMethodName, String name, List<Expression> args) {
        super(text, contextMethodName);
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.args = Objects.requireNonNull(args, "args cannot be null");
    }

    public String getName() {
        return name;
    }

    public List<Expression> getArgs() {
        return args;
    }

    @Override
    public String getResolved() {
        String resolvedArgs = args.stream().map(Expression::getResolved).collect(Collectors.joining(", "));
        return name + "(" + resolvedArgs + ");";
    }
}