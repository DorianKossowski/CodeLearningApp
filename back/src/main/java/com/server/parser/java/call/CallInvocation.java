package com.server.parser.java.call;

import com.server.parser.java.ast.expression.Expression;

import java.util.List;
import java.util.Objects;

public class CallInvocation {
    private final String text;
    private final String contextMethodName;
    private final String name;
    private final List<Expression> args;

    public CallInvocation(String text, String contextMethodName, String name, List<Expression> args) {
        this.text = Objects.requireNonNull(text, "text cannot be null");
        this.contextMethodName = Objects.requireNonNull(contextMethodName, "contextMethodName cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.args = Objects.requireNonNull(args, "args cannot be null");
    }

    public String getText() {
        return text;
    }

    public String getContextMethodName() {
        return contextMethodName;
    }

    public String getName() {
        return name;
    }

    public List<Expression> getArgs() {
        return args;
    }
}