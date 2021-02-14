package com.server.parser.java.ast.expression;

import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.ast.value.VoidValue;
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
        throw new ResolvingVoidException();
    }

    @Override
    public String getOutput() {
        throw new ResolvingVoidException();
    }

    @Override
    public Value getValue() {
        return VoidValue.INSTANCE;
    }

    @Override
    public Constant<?> getConstant() {
        throw new ResolvingVoidException();
    }
}