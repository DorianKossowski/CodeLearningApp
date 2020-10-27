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
    public boolean equalsV(Value v2) {
        return expression == v2.expression;
    }
}