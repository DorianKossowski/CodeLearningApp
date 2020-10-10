package com.server.parser.java.ast;

import java.util.Objects;

public class Literal extends Expression {
    private final Object value;

    public Literal(Object value) {
        super(Objects.requireNonNull(value, "value cannot be null").toString());
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}