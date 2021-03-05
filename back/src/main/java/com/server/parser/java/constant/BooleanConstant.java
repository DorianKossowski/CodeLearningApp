package com.server.parser.java.constant;

import com.server.parser.util.EqualityOperatorService;
import com.server.parser.util.exception.ResolvingException;

import java.util.Objects;

public class BooleanConstant extends Constant<Boolean> {

    public BooleanConstant() {
        this(new Boolean(false));
    }

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

    @Override
    public boolean equalsC(Constant<?> constant2, EqualityOperatorService.EqualityType type) {
        return EqualityOperatorService.check(this, constant2, type);
    }

    @Override
    public boolean and(Constant<?> constant2) {
        if (constant2.c instanceof Boolean) {
            return c && (Boolean) constant2.c;
        }
        throw new ResolvingException("Nie można użyć operatora && dla typu " + constant2.c.getClass().getSimpleName());
    }

    @Override
    public boolean or(Constant<?> constant2) {
        if (constant2.c instanceof Boolean) {
            return c || (Boolean) constant2.c;
        }
        throw new ResolvingException("Nie można użyć operatora || dla typu " + constant2.c.getClass().getSimpleName());
    }
}