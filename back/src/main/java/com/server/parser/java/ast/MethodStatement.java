package com.server.parser.java.ast;

import java.util.Objects;

public abstract class MethodStatement extends Statement {
    private final String contextMethodName;

    protected MethodStatement(String text, String contextMethodName) {
        super(text);
        this.contextMethodName = Objects.requireNonNull(contextMethodName, "contextMethodName cannot be null");
    }

    public String getContextMethodName() {
        return contextMethodName;
    }
}