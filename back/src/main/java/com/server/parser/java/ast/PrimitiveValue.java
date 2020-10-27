package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.util.EqualityService;

public class PrimitiveValue extends Value {
    protected final Constant<?> constant;

    public PrimitiveValue(Expression expression) {
        super(expression);
        this.constant = expression.getConstant();
    }

    public Constant<?> getConstant() {
        return constant;
    }

    @Override
    public String toString() {
        return constant.toString();
    }

    @Override
    public boolean equalsV(Value v2) {
        if (v2 instanceof ConstantProvider) {
            ConstantProvider constantProvider = (ConstantProvider) v2;
            return constant.equalsC(constantProvider.getConstant(), EqualityService.EqualityType.PRIMITIVE).c;
        }
        throw new UnsupportedOperationException();
    }
}