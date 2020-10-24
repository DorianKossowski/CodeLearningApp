package com.server.parser.java.ast.expression;

import com.server.parser.java.ast.constant.Constant;

import java.util.Objects;

public class ObjectRef extends Expression {
    private final Expression expression;

    public ObjectRef(String text, Expression expression) {
        super(text);
        this.expression = Objects.requireNonNull(expression, "value cannot be null");
    }

    public Expression getValue() {
        return expression;
    }

    @Override
    public Literal getLiteral() {
        return expression.getLiteral();
    }

    @Override
    public Constant<?> getResolved() {
        return expression.getResolved();
    }

    @Override
    public Constant<?> getConstant() {
        return expression.getConstant();
    }
}