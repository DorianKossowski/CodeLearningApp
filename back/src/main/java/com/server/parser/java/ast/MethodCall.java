package com.server.parser.java.ast;

import java.util.List;

public class MethodCall extends Statement {
    private final String name;
    private final List<Expression> args;

    public MethodCall(String name, List<Expression> args) {
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public List<Expression> getArgs() {
        return args;
    }
}