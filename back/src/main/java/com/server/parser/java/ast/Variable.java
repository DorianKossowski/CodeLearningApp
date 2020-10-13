package com.server.parser.java.ast;

import com.server.parser.util.VariableValidator;

import java.util.Objects;

public class Variable extends Statement {
    private final String type;
    private final String name;
    private final Expression value;

    public Variable(String text, String type, String name) {
        this(text, type, name, null);
    }

    public Variable(String text, String type, String name, Expression value) {
        super(text);
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        VariableValidator.validate(type, value);
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public String getResolved() {
        return String.format("%s %s = %s", type, name, value.getResolved());
    }
}