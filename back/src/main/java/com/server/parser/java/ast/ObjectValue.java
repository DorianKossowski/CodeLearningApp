package com.server.parser.java.ast;

import com.server.parser.java.ast.expression.Expression;

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
}