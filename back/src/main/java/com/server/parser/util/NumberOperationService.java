package com.server.parser.util;

import com.server.parser.java.ast.constant.BooleanConstant;
import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.constant.DoubleConstant;
import com.server.parser.java.ast.constant.IntConstant;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class NumberOperationService {
    private final static Map<String, BiFunction<Integer, Integer, Constant<?>>> intOperations = new HashMap<>();
    private final static Map<String, BiFunction<Double, Double, Constant<?>>> doubleOperations = new HashMap<>();

    static {
        intOperations.put("+", (v1, v2) -> new IntConstant(v1 + v2));
        intOperations.put("-", (v1, v2) -> new IntConstant(v1 - v2));
        intOperations.put("*", (v1, v2) -> new IntConstant(v1 * v2));
        intOperations.put("/", (v1, v2) -> new IntConstant(v1 / v2));
        intOperations.put("%", (v1, v2) -> new IntConstant(v1 % v2));
        intOperations.put("<", (v1, v2) -> new BooleanConstant(v1 < v2));
        intOperations.put("<=", (v1, v2) -> new BooleanConstant(v1 <= v2));
        intOperations.put(">", (v1, v2) -> new BooleanConstant(v1 > v2));
        intOperations.put(">=", (v1, v2) -> new BooleanConstant(v1 >= v2));

        doubleOperations.put("+", (v1, v2) -> new DoubleConstant(v1 + v2));
        doubleOperations.put("-", (v1, v2) -> new DoubleConstant(v1 - v2));
        doubleOperations.put("*", (v1, v2) -> new DoubleConstant(v1 * v2));
        doubleOperations.put("/", (v1, v2) -> new DoubleConstant(v1 / v2));
        doubleOperations.put("%", (v1, v2) -> new DoubleConstant(v1 % v2));
        doubleOperations.put("<", (v1, v2) -> new BooleanConstant(v1 < v2));
        doubleOperations.put("<=", (v1, v2) -> new BooleanConstant(v1 <= v2));
        doubleOperations.put(">", (v1, v2) -> new BooleanConstant(v1 > v2));
        doubleOperations.put(">=", (v1, v2) -> new BooleanConstant(v1 >= v2));
    }

    public static Constant<?> compute(IntConstant value1, IntConstant value2, String operator) {
        checkDivByZero(value2.c, operator);
        return intOperations.computeIfAbsent(operator, ($) -> {
            throw new UnsupportedOperationException(operator + " not supported");
        }).apply(value1.c, value2.c);
    }

    private static void checkDivByZero(Number value, String operator) {
        if (operator.equals("/") && value.doubleValue() == 0) {
            throw new ResolvingException("Dzielenie przez 0 jest niedozwolone");
        }
        if (operator.equals("%") && value.doubleValue() == 0) {
            throw new ResolvingException("Operacja modulo 0 jest niedozwolona");
        }
    }

    public static Constant<?> compute(DoubleConstant value1, DoubleConstant value2, String operator) {
        checkDivByZero(value2.c, operator);
        return doubleOperations.computeIfAbsent(operator, ($) -> {
            throw new UnsupportedOperationException(operator + " not supported");
        }).apply(value1.c, value2.c);
    }

    public static Constant<?> compute(DoubleConstant value1, IntConstant value2, String operator) {
        return compute(value1, new DoubleConstant((double) value2.c), operator);
    }

    public static Constant<?> compute(IntConstant value1, DoubleConstant value2, String operator) {
        return compute(new DoubleConstant((double) value1.c), value2, operator);
    }
}