package com.server.parser.java.ast.expression;

import com.server.parser.java.ast.Value;
import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.constant.DoubleConstant;
import com.server.parser.util.ValuePreparer;
import com.server.parser.util.exception.ResolvingException;

import java.util.Objects;

public class Literal extends Expression {
    private Constant<?> constant;

    public Literal(Constant<?> constant) {
        super(Objects.requireNonNull(constant, "constant cannot be null").toString());
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
    public Value getValue() {
        return ValuePreparer.preparePrimitive(constant.c.getClass().getSimpleName(), this);
    }

    @Override
    public Constant<?> getResolved() {
        return constant;
    }

    @Override
    public Constant<?> getConstant() {
        return constant;
    }
}