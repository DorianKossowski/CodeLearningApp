package com.server.parser.java.variable;

import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.Value;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Variable {
    private final List<String> modifiers;
    private final String type;
    private final String name;
    protected Value value;

    // only for test purpose
    Variable(String type, String name, Value value) {
        this.modifiers = Collections.emptyList();
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.value = value;
    }

    Variable(VariableDef variableDef, Value value) {
        this.modifiers = Objects.requireNonNull(variableDef, "variableDef cannot be null").getModifiers();
        this.type = variableDef.getType();
        this.name = variableDef.getName();
        this.value = value;
    }

    public List<String> getModifiers() {
        return modifiers;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public abstract Value getValue();

    public void setValue(Value value) {
        this.value = value;
    }

    public abstract void initialize(JavaContext context);
}