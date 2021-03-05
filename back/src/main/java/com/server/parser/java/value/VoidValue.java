package com.server.parser.java.value;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.VoidExpression;
import com.server.parser.util.exception.ResolvingVoidException;

public class VoidValue extends Value {
    public static final VoidValue INSTANCE = new VoidValue();

    private VoidValue() {
        super(VoidExpression.INSTANCE);
    }

    @Override
    public boolean equalsOperator(Value v2) {
        throw new ResolvingVoidException();
    }

    @Override
    public boolean equalsMethod(Value v2) {
        throw new ResolvingVoidException();
    }

    @Override
    public boolean and(Value v2) {
        throw new ResolvingVoidException();
    }

    @Override
    public boolean or(Value v2) {
        throw new ResolvingVoidException();
    }

    @Override
    public Value getAttribute(String name) {
        throw new ResolvingVoidException();
    }

    @Override
    public void updateAttribute(String name, Expression newExpression) {
        throw new ResolvingVoidException();
    }

    @Override
    public String toString() {
        return "";
    }
}