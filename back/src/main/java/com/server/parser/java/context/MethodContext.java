package com.server.parser.java.context;

import java.util.Objects;

public class MethodContext {
    private final String methodName;

    public MethodContext(String name) {
        this.methodName = Objects.requireNonNull(name, "name cannot be null");
    }

    public String getMethodName() {
        return methodName;
    }
}