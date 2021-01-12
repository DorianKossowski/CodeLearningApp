package com.server.parser.java.ast;

import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.ast.value.Value;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Variable implements Serializable {
    private final List<String> modifiers;
    private final String type;
    private final String name;
    private Value value;

    // only for test purpose
    public Variable(String type, String name, Value value) {
        this.modifiers = Collections.emptyList();
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public Variable(VariableDef variableDef) {
        this.modifiers = Objects.requireNonNull(variableDef, "variableDef cannot be null").getModifiers();
        this.type = variableDef.getType();
        this.name = variableDef.getName();
        this.value = variableDef.getValue();
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

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}