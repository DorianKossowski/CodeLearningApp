package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.BooleanConstant;
import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.expression.Literal;

public class ObjectWrapperValue extends ObjectValue implements ConstantProvider {
    protected final Constant<?> constant;

    public ObjectWrapperValue(Literal literal) {
        super(literal);
        this.constant = expression.getConstant();
    }

    @Override
    public Constant<?> getConstant() {
        return constant;
    }

    @Override
    public String toString() {
        return expression.getResolved().toString();
    }

    @Override
    public BooleanConstant equalsV(Value v2) {
        throw new UnsupportedOperationException();
    }
}