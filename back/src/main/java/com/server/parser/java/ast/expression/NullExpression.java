package com.server.parser.java.ast.expression;

import com.server.parser.java.ast.NullValue;
import com.server.parser.java.ast.Value;
import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.util.exception.ResolvingNullPointerException;

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
    public Constant<?> getResolved() {
        return new StringConstant("null");
    }

    @Override
    public Value getValue() {
        return NullValue.INSTANCE;
    }

    @Override
    public Constant<?> getConstant() {
        throw new ResolvingNullPointerException();
    }
}
