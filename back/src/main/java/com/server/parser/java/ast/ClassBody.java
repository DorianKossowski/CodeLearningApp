package com.server.parser.java.ast;

import java.util.List;
import java.util.Objects;

public class ClassBody extends AstElement {
    private final List<Method> methods;

    public ClassBody(List<Method> methods) {
        this.methods = Objects.requireNonNull(methods, "methods cannot be null");
    }

    public List<Method> getMethods() {
        return methods;
    }
}