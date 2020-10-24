package com.server.parser.java.ast.expression;

import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.constant.DoubleConstant;
import com.server.parser.util.exception.ResolvingException;

import java.util.Objects;

public class Literal extends Expression {
    private Constant<?> constant;

    public Literal(Constant<?> constant) {
        this(Objects.requireNonNull(constant, "constant cannot be null"), constant.toString());
    }

    public Literal(Constant<?> constant, String text) {
        super(text);
        this.constant = Objects.requireNonNull(constant, "constant cannot be null");
    }

    public <T> void castFromInt(Class<T> toType) {
        Integer intValue = ((Integer) constant.c);
        if (toType == Double.class) {
            constant = new DoubleConstant(intValue.doubleValue());
        } else {
            throw new ResolvingException(String.format("Rzutowanie na typ %s jest niedostępne", toType.getSimpleName()));
        }
    }

    @Override
    public Literal getLiteral() {
        return this;
    }

    @Override
    public Constant<?> getResolved() {
        return constant;
    }
}