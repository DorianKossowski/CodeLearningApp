package com.server.parser.java.ast.constant;

import com.server.parser.util.exception.ResolvingException;

import java.util.Objects;

public class BooleanConstant extends Constant<Boolean> {

    public BooleanConstant(Boolean value) {
        super(Objects.requireNonNull(value, "value cannot be null"));
    }

    @Override
    protected Constant<?> compute(IntConstant c2, String operation) {
        throw new ResolvingException("Operacje matematyczne nie są wspierane dla typu Boolean");
    }

    @Override
    protected Constant<?> compute(DoubleConstant c2, String operation) {
        throw new ResolvingException("Operacje matematyczne nie są wspierane dla typu Boolean");
    }
}