package com.server.parser.java.ast;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.util.exception.ResolvingException;

public class ObjectValue extends Value {
    // fields, methods ... ???

    public ObjectValue(Expression expression) {
        super(expression);
    }

    @Override
    public String toString() {
        return expression.getResolved().toString();
    }

    @Override
    public boolean equalsOperator(Value v2) {
        return expression == v2.expression;
    }

    @Override
    public boolean equalsMethod(Value v2) {
        return expression == v2.expression;
    }

    @Override
    public boolean and(Value v2) {
        throw new ResolvingException("Nie można wykonać operacji &&");
    }
}