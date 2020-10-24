package com.server.parser.java.ast;

import com.server.parser.java.ast.statement.VariableDef;

import java.util.Objects;

public class Variable {
    private final String type;
    private final String name;
    private Value value;

    public Variable(String type, String name, Value value) {
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public Variable(VariableDef variableDef) {
        this.type = variableDef.getType();
        this.name = variableDef.getName();
        this.value = variableDef.getValue();
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