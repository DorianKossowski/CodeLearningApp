package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;

public class PrimitiveNumberValue extends PrimitiveValue implements Computable {

    public PrimitiveNumberValue(Expression expression) {
        super(expression);
    }

    @Override
    public Computable compute(Computable v2, String operation) {
        Constant<?> computedConstant = this.constant.compute(v2.getConstant(), operation);
        return new PrimitiveNumberValue(new Literal(computedConstant));
    }
}