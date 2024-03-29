package com.server.parser.java.ast.expression;

import com.server.parser.java.value.UninitializedValue;
import com.server.parser.java.value.Value;
import com.server.parser.util.exception.ResolvingUninitializedException;

public class UninitializedExpression extends Expression {

    public UninitializedExpression(String name) {
        super(name);
    }

    @Override
    public Literal getLiteral() {
        throw new ResolvingUninitializedException(getText());
    }

    @Override
    public String getResolvedText() {
        throw new ResolvingUninitializedException(getText());
    }

    @Override
    public String getOutput() {
        throw new ResolvingUninitializedException(getText());
    }

    @Override
    public Value getValue() {
        return new UninitializedValue(this);
    }
}