package com.server.parser.util;

import com.google.common.base.Preconditions;
import com.server.parser.java.ast.*;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.util.exception.ResolvingException;

public class ValuePreparer {

    public static Value prepare(String type, Expression expression) {
        try {
            if (expression != null) {
                return prepareFromLiteral(type, expression.getLiteral());
            }
            return null; // TODO use NullValue
        } catch (IllegalArgumentException e) {
            throw new ResolvingException(String.format("Wyrażenie %s nie jest typu %s", expression.getText(), type));
        }
    }

    private static Value prepareFromLiteral(String type, Literal literal) {
        Object constant = literal.getConstant().c;
        if (Character.isUpperCase(type.charAt(0))) {
            return prepareFromObjectLiteral(type, literal, constant);
        }
        return prepareFromPrimitiveLiteral(type, literal, constant);
    }

    private static PrimitiveValue prepareFromPrimitiveLiteral(String type, Literal literal, Object constant) {
        switch (type) {
            case "char":
                Preconditions.checkArgument(constant instanceof Character);
                return new PrimitiveComputableValue(literal);
            case "int":
            case "byte":
            case "short":
            case "long":
                Preconditions.checkArgument(constant instanceof Integer);
                return new PrimitiveComputableValue(literal);
            case "float":
            case "double":
                if (constant instanceof Double) {
                    return new PrimitiveComputableValue(literal);
                }
                Preconditions.checkArgument(constant instanceof Integer);
                literal.castFromInt(Double.class);
                return new PrimitiveComputableValue(literal);
            case "boolean":
                Preconditions.checkArgument(constant instanceof Boolean);
                break;
            default:
                throw new RuntimeException(String.format("Format %s not supported", type));
        }
        return new PrimitiveValue(literal);
    }

    private static Value prepareFromObjectLiteral(String type, Literal literal, Object constant) {
        switch (type) {
            case "String":
                Preconditions.checkArgument(constant instanceof String);
                return new ObjectValue(literal);
            case "Character":
                Preconditions.checkArgument(constant instanceof Character);
                return new ObjectComputableValue(literal);
            case "Integer":
            case "Byte":
            case "Short":
            case "Long":
                Preconditions.checkArgument(constant instanceof Integer);
                return new ObjectComputableValue(literal);
            case "Float":
            case "Double":
                if (constant instanceof Double) {
                    return new ObjectComputableValue(literal);
                }
                Preconditions.checkArgument(constant instanceof Integer);
                literal.castFromInt(Double.class);
                return new ObjectComputableValue(literal);
            case "Boolean":
                Preconditions.checkArgument(constant instanceof Boolean);
                return new ObjectWrapperValue(literal);
            default:
                throw new RuntimeException(String.format("Format %s not supported", type));
        }
    }
}