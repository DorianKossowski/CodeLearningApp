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
        switch (type) {
            case "char":
                return new Literal(new CharacterConstant());
            case "int":
            case "byte":
            case "short":
            case "long":
                return new Literal(new IntConstant());
            case "float":
            case "double":
                return new Literal(new DoubleConstant());
            case "boolean":
                return new Literal(new BooleanConstant());
            default:
                throw new RuntimeException(String.format("Format %s not supported", type));
        }
    }

    public static Expression prepareUninitialized(String name) {
        return new UninitializedExpression(name);
    }
}