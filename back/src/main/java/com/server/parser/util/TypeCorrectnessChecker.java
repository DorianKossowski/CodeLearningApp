package com.server.parser.util;

import com.google.common.collect.ImmutableMap;
import com.server.parser.java.ast.expression.*;
import com.server.parser.util.exception.ResolvingException;

import java.util.Map;
import java.util.function.Predicate;

public class TypeCorrectnessChecker {
    private static final Map<ValueType, Predicate<Object>> typeToConstantChecker =
            ImmutableMap.<ValueType, Predicate<Object>>builder()
                    .put(ValueType.CHAR, constant -> constant instanceof Character)
                    .put(ValueType.INT, constant -> constant instanceof Integer)
                    .put(ValueType.BYTE, constant -> constant instanceof Integer)
                    .put(ValueType.SHORT, constant -> constant instanceof Integer)
                    .put(ValueType.LONG, constant -> constant instanceof Integer)
                    .put(ValueType.FLOAT, constant -> constant instanceof Double || constant instanceof Integer)
                    .put(ValueType.DOUBLE, constant -> constant instanceof Double || constant instanceof Integer)
                    .put(ValueType.BOOLEAN, constant -> constant instanceof Boolean)
                    .put(ValueType.STRING, constant -> constant instanceof String)
                    .put(ValueType.CHARACTER, constant -> constant instanceof Character)
                    .put(ValueType.INTEGER, constant -> constant instanceof Integer)
                    .put(ValueType.BYTE_OBJ, constant -> constant instanceof Integer)
                    .put(ValueType.SHORT_OBJ, constant -> constant instanceof Integer)
                    .put(ValueType.LONG_OBJ, constant -> constant instanceof Integer)
                    .put(ValueType.FLOAT_OBJ, constant -> constant instanceof Double || constant instanceof Integer)
                    .put(ValueType.DOUBLE_OBJ, constant -> constant instanceof Double || constant instanceof Integer)
                    .put(ValueType.BOOLEAN_OBJ, constant -> constant instanceof Boolean)
                    .build();

    public static boolean isCorrect(String type, Expression expression) {
        ValueType valueType = ValueType.findByOriginalType(type);
        if (expression instanceof NullExpression) {
            return valueType.isObjectType();
        }
        if (expression instanceof UninitializedExpression) {
            return true;
        }
        if (valueType == ValueType.VOID) {
            return expression instanceof VoidExpression;
        }
        if (valueType == ValueType.ARRAY) {
            throw new ResolvingException(String.format("Operacje na tablicach (%s) nie są wspierane", type));
        }
        if (valueType == ValueType.GENERIC) {
            return expression.getValue().getExpression() instanceof Instance;
        }
        return typeToConstantChecker.get(valueType).test(expression.getLiteral().getConstant().c);
    }
}