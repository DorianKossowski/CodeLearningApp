package com.server.parser.java.ast;

import java.util.Objects;

public class Literal extends Expression {
    protected final Object value;

    public Literal(Object value) {
        this(Objects.requireNonNull(value, "value cannot be null"), value.toString());
    }

    public Literal(Object value, String text) {
        super(text);
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public Object getValue() {
        return value;
    }

    @Override
    public Object getResolved() {
        return value;
    }
}