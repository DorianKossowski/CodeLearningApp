package com.server.parser.util;

import com.google.common.base.Preconditions;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.NullExpression;
import com.server.parser.java.ast.expression.UninitializedExpression;
import com.server.parser.java.ast.value.*;
import com.server.parser.util.exception.ResolvingException;

public class ValuePreparer {

    public static Value prepare(String type, Expression expression) {
        try {
            if (expression instanceof NullExpression) {
                return NullValue.INSTANCE;
            }
            if (expression instanceof UninitializedExpression) {
                return new UninitializedValue((UninitializedExpression) expression);
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
                Preconditions.checkArgument(constant instanceof Character);
                return new PrimitiveComputableValue(literal);
            case INT:
            case BYTE:
            case SHORT:
            case LONG:
                Preconditions.checkArgument(constant instanceof Integer);
                return new PrimitiveComputableValue(literal);
            case FLOAT:
            case DOUBLE:
                if (constant instanceof Double) {
                    return new PrimitiveComputableValue(literal);
                }
                Preconditions.checkArgument(constant instanceof Integer);
                literal.castFromInt(Double.class);
                return new PrimitiveComputableValue(literal);
            case BOOLEAN:
                Preconditions.checkArgument(constant instanceof Boolean);
                break;
            default:
                throw new RuntimeException(String.format("Format %s not supported", type.getType()));
        }
        return new PrimitiveValue(literal);
    }

    private static ObjectValue prepareFromObjectLiteral(ValueType type, Literal literal, Object constant) {
        switch (type) {
            case STRING:
                Preconditions.checkArgument(constant instanceof String);
                return new ObjectWrapperValue(literal);
            case CHARACTER:
                Preconditions.checkArgument(constant instanceof Character);
                return new ObjectComputableValue(literal);
            case INTEGER:
            case BYTE_OBJ:
            case SHORT_OBJ:
            case LONG_OBJ:
                Preconditions.checkArgument(constant instanceof Integer);
                return new ObjectComputableValue(literal);
            case FLOAT_OBJ:
            case DOUBLE_OBJ:
                if (constant instanceof Double) {
                    return new ObjectComputableValue(literal);
                }
                Preconditions.checkArgument(constant instanceof Integer);
                literal.castFromInt(Double.class);
                return new ObjectComputableValue(literal);
            case BOOLEAN_OBJ:
                Preconditions.checkArgument(constant instanceof Boolean);
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