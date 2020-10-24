package com.server.parser.java.ast;

import com.server.parser.java.ast.statement.VariableDef;

import java.util.List;
import java.util.Objects;

public class ClassBody extends AstElement {
    private final List<VariableDef> fields;
    private final List<Method> methods;

    public ClassBody(List<VariableDef> fields, List<Method> methods) {
        this.fields = Objects.requireNonNull(fields, "fields cannot be null");
        this.methods = Objects.requireNonNull(methods, "methods cannot be null");
    }

    public List<Method> getMethods() {
        return methods;
    }

    public List<VariableDef> getFields() {
        return fields;
    }
}