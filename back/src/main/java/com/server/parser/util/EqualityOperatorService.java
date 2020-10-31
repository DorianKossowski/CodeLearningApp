package com.server.parser.util;

import com.server.parser.java.ast.constant.*;
import com.server.parser.util.exception.ResolvingException;

import java.util.function.BiFunction;

// ONLY == operator
public class EqualityOperatorService {
    public enum EqualityType {
        PRIMITIVE, OBJECT
    }

    private static final BiFunction<Object, Object, Boolean> OBJECT_EQUAL = (c1, c2) -> c1 == c2;

    public static BooleanConstant check(BooleanConstant constant1, Constant<?> constant2, EqualityType type) {
        if (constant2 instanceof BooleanConstant) {
            if (type == EqualityType.PRIMITIVE) {
                return new BooleanConstant(constant1.c.booleanValue() == ((Boolean) constant2.c));
            }
            return new BooleanConstant(OBJECT_EQUAL.apply(constant1.c, constant2.c));
        }
        throw new ResolvingException("Nie można porównać typu logicznego z " + constant2.c.getClass().getSimpleName());
    }

    public static BooleanConstant check(CharacterConstant constant1, Constant<?> constant2, EqualityType type) {
        Object c2 = constant2.c;
        if (type == EqualityType.PRIMITIVE) {
            char charValue = constant1.c;
            boolean result;
            if (c2 instanceof Character) {
                result = charValue == (Character) c2;
            } else if (c2 instanceof Integer) {
                result = charValue == (Integer) c2;
            } else if (c2 instanceof Double) {
                result = charValue == (Double) c2;
            } else {
                throw new ResolvingException("Nie można porównać typu znakowego z " + c2.getClass().getSimpleName());
            }
            return new BooleanConstant(result);
        }
        if (constant2 instanceof CharacterConstant) {
            return new BooleanConstant(OBJECT_EQUAL.apply(constant1.c, c2));
        }
        throw new ResolvingException("Nie można porównać typu znakowego z " + c2.getClass().getSimpleName());
    }

    public static BooleanConstant check(DoubleConstant constant1, Constant<?> constant2, EqualityType type) {
        Object c2 = constant2.c;
        if (type == EqualityType.PRIMITIVE) {
            double doubleValue = constant1.c;
            boolean result;
            if (c2 instanceof Character) {
                result = doubleValue == (Character) c2;
            } else if (c2 instanceof Integer) {
                result = doubleValue == (Integer) c2;
            } else if (c2 instanceof Double) {
                result = doubleValue == (Double) c2;
            } else {
                throw new ResolvingException("Nie można porównać typu dziesiętnego z " + c2.getClass().getSimpleName());
            }
            return new BooleanConstant(result);
        }
        if (constant2 instanceof DoubleConstant) {
            return new BooleanConstant(OBJECT_EQUAL.apply(constant1.c, c2));
        }
        throw new ResolvingException("Nie można porównać typu dziesiętnego z " + constant2.c.getClass().getSimpleName());
    }

    public static BooleanConstant check(IntConstant constant1, Constant<?> constant2, EqualityType type) {
        Object c2 = constant2.c;
        if (type == EqualityType.PRIMITIVE) {
            int intValue = constant1.c;
            boolean result;
            if (c2 instanceof Character) {
                result = intValue == (Character) c2;
            } else if (c2 instanceof Integer) {
                result = intValue == (Integer) c2;
            } else if (c2 instanceof Double) {
                result = intValue == (Double) c2;
            } else {
                throw new ResolvingException("Nie można porównać typu całkowitego z " + c2.getClass().getSimpleName());
            }
            return new BooleanConstant(result);
        }
        if (constant2 instanceof IntConstant) {
            return new BooleanConstant(OBJECT_EQUAL.apply(constant1.c, c2));
        }
        throw new ResolvingException("Nie można porównać typu całkowitego z " + constant2.c.getClass().getSimpleName());
    }

    public static BooleanConstant check(StringConstant constant1, Constant<?> constant2) {
        if (constant2 instanceof StringConstant) {
            return new BooleanConstant(OBJECT_EQUAL.apply(constant1.c, constant2.c));
        }
        throw new ResolvingException("Nie można porównać typu String z " + constant2.c.getClass().getSimpleName());
    }
}
