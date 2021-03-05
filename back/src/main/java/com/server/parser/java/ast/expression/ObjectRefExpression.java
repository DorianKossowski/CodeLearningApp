package com.server.parser.java.ast.expression;

import com.server.parser.java.ast.value.Value;

import java.util.Objects;

public class ObjectRefExpression extends Expression {
    private final Value value;
    private final Expression expression;

    public ObjectRefExpression(String text, Value value) {
        super(text);
        this.value = Objects.requireNonNull(value, "value cannot be null");
        this.expression = Objects.requireNonNull(value.getExpression(), "expression cannot be null");
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public Literal getLiteral() {
        return expression.getLiteral();
    }

    @Override
    public String getResolvedText() {
        return expression.getResolvedText();
    }

    @Override
    public String getOutput() {
        return expression.getOutput();
    }
}