package com.server.parser.java.ast;

import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.util.EqualityOperatorService;
import com.server.parser.util.exception.ResolvingException;
import com.server.parser.util.exception.ResolvingNullPointerException;

public class ObjectWrapperValue extends ObjectValue implements ConstantProvider {
    protected final Constant<?> constant;

    public ObjectWrapperValue(Literal literal) {
        super(literal);
        this.constant = expression.getConstant();
    }

    @Override
    public Constant<?> getConstant() {
        return constant;
    }

    @Override
    public String toString() {
        return expression.getResolved().toString();
    }

    @Override
    public boolean equalsOperator(Value v2) {
        if (v2 instanceof PrimitiveValue) {
            PrimitiveValue primitiveValue = (PrimitiveValue) v2;
            return constant.equalsC(primitiveValue.getConstant(), EqualityOperatorService.EqualityType.PRIMITIVE);
        }
        if (v2 instanceof ObjectWrapperValue) {
            ObjectWrapperValue wrapperValue = (ObjectWrapperValue) v2;
            return constant.equalsC(wrapperValue.getConstant(), EqualityOperatorService.EqualityType.OBJECT);
        }
        if (v2 instanceof NullValue) {
            return false;
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equalsMethod(Value v2) {
        if (v2 instanceof NullValue) {
            throw new ResolvingNullPointerException();
        }
        if (v2 instanceof ConstantProvider) {
            return constant.c.equals(((ConstantProvider) v2).getConstant().c);
        }
        return false;
    }

    @Override
    public boolean and(Value v2) {
        if (v2 instanceof ConstantProvider) {
            ConstantProvider constantProvider = (ConstantProvider) v2;
            return constant.and(constantProvider.getConstant());
        }
        throw new ResolvingException("Nie można wykonać operacji &&");
    }

    @Override
    public boolean or(Value v2) {
        if (v2 instanceof ConstantProvider) {
            ConstantProvider constantProvider = (ConstantProvider) v2;
            return constant.or(constantProvider.getConstant());
        }
        throw new ResolvingException("Nie można wykonać operacji ||");
    }
}