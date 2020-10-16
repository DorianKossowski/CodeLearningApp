package com.server.parser.java.ast.constant;

import com.server.parser.util.exception.ResolvingException;

import java.util.Objects;

public abstract class Constant<T> {
    protected final T value;

    protected Constant(T value) {
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public T getValue() {
        return value;
    }

    protected abstract Constant<?> compute(IntConstant c2, String operation);

    protected abstract Constant<?> compute(DoubleConstant c2, String operation);

    public <T2> Constant<?> compute(Constant<T2> c2, String operation) {
        if (c2.value instanceof Integer) {
            return compute(((IntConstant) c2), operation);
        }
        if (c2.value instanceof Double) {
            return compute(((DoubleConstant) c2), operation);
        }
        throw new ResolvingException("Operacje matematyczne nie są wspierane dla typu " + c2.value.getClass().getSimpleName());
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        Constant<?> constant = (Constant<?>) o;
        if (o == null || value.getClass() != constant.value.getClass()) {
            return false;
        }
        return Objects.equals(value, constant.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
