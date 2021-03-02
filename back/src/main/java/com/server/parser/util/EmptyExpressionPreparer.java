package com.server.parser.util;

import com.server.parser.java.ast.constant.BooleanConstant;
import com.server.parser.java.ast.constant.CharacterConstant;
import com.server.parser.java.ast.constant.DoubleConstant;
import com.server.parser.java.ast.constant.IntConstant;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.NullExpression;
import com.server.parser.java.ast.expression.UninitializedExpression;

public class EmptyExpressionPreparer {

    public static Expression prepare(String type) {
        if (Character.isUpperCase(type.charAt(0))) {
            return NullExpression.INSTANCE;
        }
        ValueType valueType = ValueType.findByOriginalType(type);
        switch (valueType) {
            case CHAR:
                return new Literal(new CharacterConstant());
            case INT:
            case BYTE:
            case SHORT:
            case LONG:
                return new Literal(new IntConstant());
            case FLOAT:
            case DOUBLE:
                return new Literal(new DoubleConstant());
            case BOOLEAN:
                return new Literal(new BooleanConstant());
            default:
                throw new RuntimeException(String.format("Format %s not supported", type));
        }
    }

    public static Expression prepareUninitialized(String name) {
        return new UninitializedExpression(name);
    }
}