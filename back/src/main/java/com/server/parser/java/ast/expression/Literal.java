package com.server.parser.java.ast.expression;

import com.server.parser.java.constant.Constant;
import com.server.parser.java.constant.DoubleConstant;
import com.server.parser.java.value.Value;
import com.server.parser.java.value.util.ValuePreparer;
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
    public String getResolvedText() {
        return constant.toString();
    }

    @Override
    public String getOutput() {
        return constant.getRawValue();
    }

    public Constant<?> getConstant() {
        return constant;
    }
}