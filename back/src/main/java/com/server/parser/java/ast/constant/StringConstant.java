package com.server.parser.java.ast.constant;

import com.server.parser.util.EqualityService;
import com.server.parser.util.exception.ResolvingException;

import java.util.Objects;

public class StringConstant extends Constant<String> {

    public StringConstant(String value) {
        super(Objects.requireNonNull(value, "value cannot be null"));
    }

    @Override
    public String toString() {
        return '"' + super.toString() + '"';
    }

    @Override
    public BooleanConstant equalsC(Constant<?> constant2, EqualityService.EqualityType type) {
        return EqualityService.check(this, constant2);
    }

    @Override
    protected Constant<?> compute(IntConstant c2, String operation) {
        throw new ResolvingException("Operacje matematyczne nie są wspierane dla typu String");
    }

    @Override
    protected Constant<?> compute(DoubleConstant c2, String operation) {
        throw new ResolvingException("Operacje matematyczne nie są wspierane dla typu String");
    }
}