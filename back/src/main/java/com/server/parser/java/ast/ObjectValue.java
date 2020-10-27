package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.BooleanConstant;
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
    public BooleanConstant equalsV(Value v2) {
        return new BooleanConstant(expression == v2.expression);
    }
}