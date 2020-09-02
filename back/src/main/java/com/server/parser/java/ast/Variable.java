package com.server.parser.java.ast;

import java.util.Objects;

public class Variable extends AstElement {
    private final String type;
    private final String name;
    private final Expression value;

    public Variable(String type, String name) {
        this(type, name, null);
    }

    public Variable(String type, String name, Expression value) {
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
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
}