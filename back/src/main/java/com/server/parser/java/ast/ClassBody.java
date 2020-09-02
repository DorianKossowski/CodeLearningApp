package com.server.parser.java.ast;

import java.util.List;
import java.util.Objects;

public class ClassBody extends AstElement {
    private final List<Method> methods;
    private final List<Variable> fields;

    public ClassBody(List<Method> methods, List<Variable> fields) {
        this.methods = Objects.requireNonNull(methods, "methods cannot be null");
        this.fields = Objects.requireNonNull(fields, "fields cannot be null");
    }

    public List<Method> getMethods() {
        return methods;
    }

    public List<Variable> getFields() {
        return fields;
    }
}