package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.Constant;

import java.util.Objects;

public class Literal extends Expression {
    protected final Constant<?> value;

    public Literal(Constant<?> value) {
        this(Objects.requireNonNull(value, "value cannot be null"), value.toString());
    }

    public Literal(Constant<?> value, String text) {
        super(text);
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public Constant<?> getValue() {
        return value;
    }

    @Override
    public Object getResolved() {
        return value.getValue();
    }
}