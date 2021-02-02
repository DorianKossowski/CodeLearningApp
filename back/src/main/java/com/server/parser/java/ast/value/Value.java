package com.server.parser.java.ast.value;

import com.server.parser.java.ast.expression.Expression;

import java.io.Serializable;
import java.util.Objects;

public abstract class Value implements EqualityCheckable, LogicOperational, Serializable {
    //TODO creation expression <- Literal, Constructor?
    protected final Expression expression;

    protected Value(Expression expression) {
        this.expression = Objects.requireNonNull(expression, "expression cannot be null");
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public abstract String toString();
}