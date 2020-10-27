package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.expression.Literal;

public class PrimitiveComputableValue extends PrimitiveValue implements Computable {

    public PrimitiveComputableValue(Literal literal) {
        super(literal);
    }

    @Override
    public Computable compute(Computable v2, String operation) {
        Constant<?> computedConstant = this.constant.compute(v2.getConstant(), operation);
        return new PrimitiveComputableValue(new Literal(computedConstant));
    }
}