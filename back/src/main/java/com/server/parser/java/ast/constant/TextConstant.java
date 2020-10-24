package com.server.parser.java.ast.constant;

import com.server.parser.util.exception.ResolvingException;

import java.util.Objects;

public abstract class TextConstant<T> extends Constant<T> {

    public TextConstant(T value) {
        super(Objects.requireNonNull(value, "value cannot be null"));
    }

    @Override
    protected Constant<?> compute(IntConstant c2, String operation) {
        throw new ResolvingException("Operacje matematyczne nie są wspierane dla typu " + c.getClass().getSimpleName());
    }

    @Override
    protected Constant<?> compute(DoubleConstant c2, String operation) {
        throw new ResolvingException("Operacje matematyczne nie są wspierane dla typu " + c.getClass().getSimpleName());
    }
}