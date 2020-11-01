package com.server.parser.java.ast;

import com.server.parser.java.ast.expression.NullExpression;
import com.server.parser.util.exception.ResolvingNullPointerException;

public class NullValue extends Value {
    public static final NullValue INSTANCE = new NullValue();

    private NullValue() {
        super(NullExpression.INSTANCE);
    }

    @Override
    public boolean equalsOperator(Value v2) {
        if (v2 instanceof NullValue) {
            return true;
        }
        if (v2 instanceof ObjectValue) {
            return false;
        }
        throw new ResolvingNullPointerException();
    }

    @Override
    public boolean equalsMethod(Value v2) {
        throw new ResolvingNullPointerException();
    }

    @Override
    public boolean and(Value v2) {
        throw new ResolvingNullPointerException();
    }

    @Override
    public boolean or(Value v2) {
        throw new ResolvingNullPointerException();
    }
}
