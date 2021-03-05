package com.server.parser.java.ast.value;

import com.server.parser.java.ast.ConstantProvider;
import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.util.EqualityOperatorService;
import com.server.parser.util.exception.ResolvingException;
import com.server.parser.util.exception.ResolvingUninitializedException;
import com.server.parser.util.exception.ResolvingVoidException;

public class ObjectWrapperValue extends ObjectValue implements ConstantProvider {
    protected final Constant<?> constant;

    public ObjectWrapperValue(Literal literal) {
        super(literal);
        this.constant = literal.getConstant();
    }

    @Override
    public Constant<?> getConstant() {
        return constant;
    }

    @Override
    public String toString() {
        return expression.getResolvedText();
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
        if (v2 instanceof UninitializedValue) {
            throw new ResolvingUninitializedException(v2.expression.getText());
        }
        if (v2 instanceof VoidValue) {
            throw new ResolvingVoidException();
        }
        if (v2 instanceof ObjectValue) {
            throw new ResolvingException("Nie można porównać z " + v2.getExpression().getResolvedText());
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
        if (v2 instanceof UninitializedValue) {
            throw new ResolvingUninitializedException(v2.expression.getText());
        }
        if (v2 instanceof VoidValue) {
            throw new ResolvingVoidException();
        }
        throw new ResolvingException("Nie można wykonać operacji &&");
    }

    @Override
    public boolean or(Value v2) {
        if (v2 instanceof ConstantProvider) {
            ConstantProvider constantProvider = (ConstantProvider) v2;
            return constant.or(constantProvider.getConstant());
        }
        if (v2 instanceof UninitializedValue) {
            throw new ResolvingUninitializedException(v2.expression.getText());
        }
        if (v2 instanceof VoidValue) {
            throw new ResolvingVoidException();
        }
        throw new ResolvingException("Nie można wykonać operacji ||");
    }
}