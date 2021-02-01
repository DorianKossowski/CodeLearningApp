package com.server.parser.java.ast.expression;

import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.value.UninitializedValue;
import com.server.parser.java.ast.value.Value;
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

    @Override
    public Constant<?> getConstant() {
        throw new ResolvingUninitializedException(getText());
    }
}