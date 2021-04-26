package com.server.parser.java.ast.expression;

import com.server.parser.java.value.NullValue;
import com.server.parser.java.value.Value;

public class NullExpression extends Expression {
    public static final NullExpression INSTANCE = new NullExpression();

    private NullExpression() {
        super("null");
    }

    @Override
    public Literal getLiteral() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getResolvedText() {
        return "null";
    }

    @Override
    public String getOutput() {
        return "null";
    }

    @Override
    public Value getValue() {
        return NullValue.INSTANCE;
    }
}
