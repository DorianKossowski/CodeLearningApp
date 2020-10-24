package com.server.parser.java.ast.constant;

import com.server.parser.util.exception.ResolvingException;

import java.util.Objects;

public abstract class Constant<T> {
    public final T c;

    protected Constant(T c) {
        this.c = Objects.requireNonNull(c, "c cannot be null");
    }

    protected abstract Constant<?> compute(IntConstant c2, String operation);

    protected abstract Constant<?> compute(DoubleConstant c2, String operation);

    public <T2> Constant<?> compute(Constant<T2> c2, String operation) {
        if (c2.c instanceof Integer) {
            return compute(((IntConstant) c2), operation);
        }
        if (c2.c instanceof Double) {
            return compute(((DoubleConstant) c2), operation);
        }
        throw new ResolvingException("Operacje matematyczne nie są wspierane dla typu " + c2.c.getClass().getSimpleName());
    }

    @Override
    public String toString() {
        return c.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        Constant<?> constant = (Constant<?>) o;
        if (o == null || c.getClass() != constant.c.getClass()) {
            return false;
        }
        return Objects.equals(c, constant.c);
    }

    @Override
    public int hashCode() {
        return Objects.hash(c);
    }
}
