package com.server.parser.java.ast;

import java.util.Objects;

public class ObjectRef extends Expression {
    private final String value;

    public ObjectRef(String text, String value) {
        super(text);
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    @Override
    public String getResolved() {
        return value;
    }
}