package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.expression.Literal;

public class ObjectNumberValue extends ObjectValue implements Computable {
    private final Constant<?> constant;

    public ObjectNumberValue(Literal literal) {
        super(literal);
        this.constant = literal.getConstant();
    }

    @Override
    public Constant<?> getConstant() {
        return constant;
    }

    @Override
    public Computable compute(Computable v2, String operation) {
        Constant<?> computedConstant = this.constant.compute(v2.getConstant(), operation);
        return new ObjectNumberValue(new Literal(computedConstant));
    }

    @Override
    public String toString() {
        return constant.toString();
    }
}