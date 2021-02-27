package com.server.parser.java.ast.value;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.util.exception.ResolvingException;
import com.server.parser.util.exception.ResolvingUninitializedException;
import com.server.parser.util.exception.ResolvingVoidException;

public class ObjectValue extends Value {
    // fields, methods ... ???

    public ObjectValue(Expression expression) {
        super(expression);
    }

    @Override
    public String toString() {
        return expression.getResolvedText();
    }

    @Override
    public boolean equalsOperator(Value v2) {
        if (v2 instanceof NullValue) {
            return false;
        }
        if (v2 instanceof UninitializedValue) {
            throw new ResolvingUninitializedException(v2.expression.getText());
        }
        if (v2 instanceof VoidValue) {
            throw new ResolvingVoidException();
        }
        if (v2 instanceof PrimitiveValue || v2 instanceof ObjectWrapperValue) {
            throw new ResolvingException("Nie można porównać typu obiektowego z " + v2.getExpression().getResolvedText());
        }
        if (v2 instanceof ObjectValue) {
            return expression == v2.expression;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equalsMethod(Value v2) {
        if (v2 instanceof NullValue) {
            return false;
        }
        if (v2 instanceof UninitializedValue) {
            throw new ResolvingUninitializedException(v2.expression.getText());
        }
        if (v2 instanceof VoidValue) {
            throw new ResolvingVoidException();
        }
        return expression == v2.expression;
    }

    @Override
    public boolean and(Value v2) {
        throw new ResolvingException("Nie można wykonać operacji &&");
    }

    @Override
    public boolean or(Value v2) {
        throw new ResolvingException("Nie można wykonać operacji ||");
    }
}