package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.Constant;

import java.util.Objects;

public class PrimitiveValue extends Value {
    private final Constant<?> constant;

    public PrimitiveValue(Constant<?> constant) {
        this.constant = Objects.requireNonNull(constant, "constant cannot be null");
    }

    public Constant<?> getConstant() {
        return constant;
    }

    @Override
    public String getResolved() {
        return constant.toString();
    }
}