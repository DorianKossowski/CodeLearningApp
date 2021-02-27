package com.server.parser.util;

import com.google.common.base.Preconditions;
import com.server.parser.java.ast.expression.*;
import com.server.parser.java.ast.value.*;
import com.server.parser.util.exception.ResolvingException;

public class ValuePreparer {

    public static Value prepare(String type, Expression expression) {
        try {
            Preconditions.checkArgument(TypeCorrectnessChecker.isCorrect(type, expression));
            if (expression instanceof NullExpression) {
                return NullValue.INSTANCE;
            }
            if (expression instanceof UninitializedExpression) {
                return new UninitializedValue((UninitializedExpression) expression);
            }
            if (expression instanceof Instance) {
                return new ObjectValue(expression);
            }
            ValueType valueType = ValueType.findByOriginalType(type);
            return prepareFromLiteral(valueType, expression.getLiteral());
        } catch (IllegalArgumentException e) {
            throw new ResolvingException(String.format("Wyrażenie %s nie jest typu %s", expression.getText(), type));
        }
    }

    private static Value prepareFromLiteral(ValueType type, Literal literal) {
        Object constant = literal.getConstant().c;
        if (Character.isUpperCase(type.getType().charAt(0))) {
            return prepareFromObjectLiteral(type, literal, constant);
        }
        return prepareFromPrimitiveLiteral(type, literal, constant);
    }

    private static PrimitiveValue prepareFromPrimitiveLiteral(ValueType type, Literal literal, Object constant) {
        switch (type) {
            case CHAR:
            case INT:
            case BYTE:
            case SHORT:
            case LONG:
                return new PrimitiveComputableValue(literal);
            case FLOAT:
            case DOUBLE:
                if (constant instanceof Double) {
                    return new PrimitiveComputableValue(literal);
                }
                literal.castFromInt(Double.class);
                return new PrimitiveComputableValue(literal);
            case BOOLEAN:
                return new PrimitiveValue(literal);
            default:
                throw new RuntimeException(String.format("Format %s not supported", type.getType()));
        }
    }

    private static ObjectValue prepareFromObjectLiteral(ValueType type, Literal literal, Object constant) {
        switch (type) {
            case CHARACTER:
            case INTEGER:
            case BYTE_OBJ:
            case SHORT_OBJ:
            case LONG_OBJ:
                return new ObjectComputableValue(literal);
            case FLOAT_OBJ:
            case DOUBLE_OBJ:
                if (constant instanceof Double) {
                    return new ObjectComputableValue(literal);
                }
                literal.castFromInt(Double.class);
                return new ObjectComputableValue(literal);
            case BOOLEAN_OBJ:
            case STRING:
                return new ObjectWrapperValue(literal);
            default:
                throw new RuntimeException(String.format("Format %s not supported", type.getType()));
        }
    }

    public static PrimitiveValue preparePrimitive(String type, Literal literal) {
        ValueType valueType = ValueType.findByOriginalType(type);
        ObjectValue value = prepareFromObjectLiteral(valueType, literal, literal.getConstant().c);
        if (value instanceof ObjectComputableValue) {
            return new PrimitiveComputableValue(value.getExpression().getLiteral());
        }
        if (value instanceof ObjectWrapperValue) {
            return new PrimitiveValue(value.getExpression());
        }
        throw new UnsupportedOperationException();
    }
}