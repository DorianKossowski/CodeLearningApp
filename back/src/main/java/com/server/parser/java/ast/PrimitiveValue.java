package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.expression.Expression;

public class PrimitiveValue extends Value {
    private final Constant<?> constant;

    public PrimitiveValue(Expression expression) {
        super(expression);
        this.constant = expression.getResolved();
    }

    public Constant<?> getConstant() {
        return constant;
    }

    @Override
    public String getResolved() {
        return constant.toString();
    }
}