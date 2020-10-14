package com.server.parser.util;

import com.google.common.base.Preconditions;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Literal;
import com.server.parser.java.ast.ObjectRef;
import com.server.parser.util.exception.ResolvingException;

public class VariableValidator {

    public static void validate(String type, Expression expression) {
        try {
            if (expression instanceof Literal) {
                validateFromLiteral(type, ((Literal) expression));
            } else if (expression instanceof ObjectRef) {
                validateFromObjectRef(type, ((ObjectRef) expression));
            }
        } catch (IllegalArgumentException e) {
            throw new ResolvingException(String.format("Wyrażenie %s nie jest typu %s", expression.getText(), type));
        }
    }

    private static void validateFromObjectRef(String type, ObjectRef objectRef) {
        validateFromLiteral(type, ((Literal) objectRef.getValue()));
    }

    private static void validateFromLiteral(String type, Literal literal) {
        switch (type) {
            case "String":
                Preconditions.checkArgument(literal.getValue() instanceof String);
                break;
            case "char":
            case "Character":
                Preconditions.checkArgument(literal.getValue() instanceof Character);
                break;
            case "int":
            case "Integer":
            case "byte":
            case "Byte":
            case "short":
            case "Short":
                Preconditions.checkArgument(literal.getValue() instanceof Integer);
                break;
        }
    }
}