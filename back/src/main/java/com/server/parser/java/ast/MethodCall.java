package com.server.parser.java.ast;

import java.util.List;
import java.util.Objects;

public class MethodCall extends MethodStatement {
    private final String name;
    private final List<Expression> args;

    public MethodCall(String contextMethodName, String name, List<Expression> args) {
        super(Objects.requireNonNull(contextMethodName, "contextMethodName cannot be null"));
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.args = Objects.requireNonNull(args, "args cannot be null");
    }

    public String getName() {
        return name;
    }

    public List<Expression> getArgs() {
        return args;
    }
}