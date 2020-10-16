package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.Constant;

import java.util.Objects;

public class Literal extends Expression {
    protected final Constant<?> constant;

    public Literal(Constant<?> constant) {
        this(Objects.requireNonNull(constant, "constant cannot be null"), constant.toString());
    }

    public Literal(Constant<?> constant, String text) {
        super(text);
        this.constant = Objects.requireNonNull(constant, "constant cannot be null");
    }

    public Constant<?> getConstant() {
        return constant;
    }

    @Override
    public Object getResolved() {
        return constant.getValue();
    }
}