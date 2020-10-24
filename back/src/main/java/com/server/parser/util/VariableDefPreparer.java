package com.server.parser.util;

import com.google.common.base.Preconditions;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.util.exception.ResolvingException;

import java.util.Optional;

public class VariableDefPreparer {

    public static void prepare(String type, Expression expression) {
        try {
            Optional.ofNullable(expression).ifPresent(e -> prepareFromLiteral(type, e.getLiteral()));
        } catch (IllegalArgumentException e) {
            throw new ResolvingException(String.format("Wyrażenie %s nie jest typu %s", expression.getText(), type));
        }
    }

    private static void prepareFromLiteral(String type, Literal literal) {
        Object constant = literal.getResolved().c;
        switch (type) {
            case "String":
                Preconditions.checkArgument(constant instanceof String);
                break;
            case "char":
            case "Character":
                Preconditions.checkArgument(constant instanceof Character);
                break;
            case "int":
            case "Integer":
            case "byte":
            case "Byte":
            case "short":
            case "Short":
            case "long":
            case "Long":
                Preconditions.checkArgument(constant instanceof Integer);
                break;
            case "float":
            case "Float":
            case "double":
            case "Double":
                if (constant instanceof Double) {
                    break;
                }
                Preconditions.checkArgument(constant instanceof Integer);
                literal.castFromInt(Double.class);
                break;
            case "boolean":
            case "Boolean":
                Preconditions.checkArgument(constant instanceof Boolean);
                break;
            default:
                throw new RuntimeException(String.format("Format %s not supported", type));
        }
    }
}