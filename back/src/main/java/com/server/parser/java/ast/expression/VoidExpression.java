package com.server.parser.java.ast.expression;

import com.server.parser.java.value.Value;
import com.server.parser.java.value.VoidValue;
import com.server.parser.util.exception.ResolvingVoidException;

public class VoidExpression extends Expression {
    public static final VoidExpression INSTANCE = new VoidExpression();

    private VoidExpression() {
        super("void");
    }

    @Override
    public Literal getLiteral() {
        throw new ResolvingVoidException();
    }

    @Override
    public String getResolvedText() {
        return "";
    }

    @Override
    public String getOutput() {
        throw new ResolvingVoidException();
    }

    @Override
    public Value getValue() {
        return VoidValue.INSTANCE;
    }
}