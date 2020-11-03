package com.server.parser.java.ast.value;

import com.server.parser.java.ast.expression.UninitializedExpression;
import com.server.parser.util.exception.ResolvingUninitializedException;

public class UninitializedValue extends Value {

    public UninitializedValue(UninitializedExpression expression) {
        super(expression);
    }

    @Override
    public boolean equalsOperator(Value v2) {
        throw new ResolvingUninitializedException(expression.getText());
    }

    @Override
    public boolean equalsMethod(Value v2) {
        throw new ResolvingUninitializedException(expression.getText());
    }

    @Override
    public boolean and(Value v2) {
        throw new ResolvingUninitializedException(expression.getText());
    }

    @Override
    public boolean or(Value v2) {
        throw new ResolvingUninitializedException(expression.getText());
    }
}