package com.server.parser.java.ast.value;

import com.server.parser.java.ast.Computable;
import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.expression.Literal;

public class ObjectComputableValue extends ObjectWrapperValue implements Computable {

    public ObjectComputableValue(Literal literal) {
        super(literal);
    }

    @Override
    public Computable compute(Computable v2, String operation) {
        Constant<?> computedConstant = this.constant.compute(v2.getConstant(), operation);
        return new ObjectComputableValue(new Literal(computedConstant));
    }

    @Override
    public String toString() {
        return constant.toString();
    }
}