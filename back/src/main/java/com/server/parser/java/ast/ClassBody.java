package com.server.parser.java.ast;

import java.util.List;
import java.util.Objects;

public class ClassBody extends AstElement {
    private final List<Variable> fields;
    private final List<Method> methods;

    public ClassBody(List<Variable> fields, List<Method> methods) {
        this.fields = Objects.requireNonNull(fields, "fields cannot be null");
        this.methods = Objects.requireNonNull(methods, "methods cannot be null");
    }

    public List<Method> getMethods() {
        return methods;
    }

    public List<Variable> getFields() {
        return fields;
    }
}