package com.server.parser.java.ast;

import java.util.List;
import java.util.Objects;

public class MethodCall extends Statement {
    private final String name;
    private final List<Expression> args;

    public MethodCall(String name, List<Expression> args) {
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