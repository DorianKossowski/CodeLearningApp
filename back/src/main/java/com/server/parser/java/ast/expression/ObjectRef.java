package com.server.parser.java.ast.expression;

import com.server.parser.java.ast.constant.Constant;

import java.util.Objects;

public class ObjectRef extends Expression {
    private final Expression value;

    public ObjectRef(String text, Expression value) {
        super(text);
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public Constant<?> getResolved() {
        return value.getResolved();
    }
}