package com.server.parser.java.ast.value;

import com.server.parser.java.ast.expression.Expression;

import java.util.Objects;

public abstract class Value implements EqualityCheckable, LogicOperational {
    //TODO creation expression <- Literal, Constructor?
    protected final Expression expression;

    protected Value(Expression expression) {
        this.expression = Objects.requireNonNull(expression, "expression cannot be null");
    }

    public Expression getExpression() {
        return expression;
    }
}