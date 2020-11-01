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

    public static boolean check(BooleanConstant constant1, Constant<?> constant2, EqualityType type) {
        if (constant2 instanceof BooleanConstant) {
            if (type == EqualityType.PRIMITIVE) {
                return constant1.c.booleanValue() == ((Boolean) constant2.c);
            }
            return OBJECT_EQUAL.apply(constant1.c, constant2.c);
        }
        throw new ResolvingException("Nie można porównać typu logicznego z " + constant2.c.getClass().getSimpleName());
    }

    public static boolean check(CharacterConstant constant1, Constant<?> constant2, EqualityType type) {
        Object c2 = constant2.c;
        if (type == EqualityType.PRIMITIVE) {
            char charValue = constant1.c;
            if (c2 instanceof Character) {
                return charValue == (Character) c2;
            }
            if (c2 instanceof Integer) {
                return charValue == (Integer) c2;
            }
            if (c2 instanceof Double) {
                return charValue == (Double) c2;
            }
            throw new ResolvingException("Nie można porównać typu znakowego z " + c2.getClass().getSimpleName());
        }
        if (constant2 instanceof CharacterConstant) {
            return OBJECT_EQUAL.apply(constant1.c, c2);
        }
        throw new ResolvingException("Nie można porównać typu znakowego z " + c2.getClass().getSimpleName());
    }

    public static boolean check(DoubleConstant constant1, Constant<?> constant2, EqualityType type) {
        Object c2 = constant2.c;
        if (type == EqualityType.PRIMITIVE) {
            double doubleValue = constant1.c;
            if (c2 instanceof Character) {
                return doubleValue == (Character) c2;
            }
            if (c2 instanceof Integer) {
                return doubleValue == (Integer) c2;
            }
            if (c2 instanceof Double) {
                return doubleValue == (Double) c2;
            }
            throw new ResolvingException("Nie można porównać typu dziesiętnego z " + c2.getClass().getSimpleName());

        }
        if (constant2 instanceof DoubleConstant) {
            return OBJECT_EQUAL.apply(constant1.c, c2);
        }
        throw new ResolvingException("Nie można porównać typu dziesiętnego z " + constant2.c.getClass().getSimpleName());
    }

    public static boolean check(IntConstant constant1, Constant<?> constant2, EqualityType type) {
        Object c2 = constant2.c;
        if (type == EqualityType.PRIMITIVE) {
            int intValue = constant1.c;
            if (c2 instanceof Character) {
                return intValue == (Character) c2;
            }
            if (c2 instanceof Integer) {
                return intValue == (Integer) c2;
            }
            if (c2 instanceof Double) {
                return intValue == (Double) c2;
            }
            throw new ResolvingException("Nie można porównać typu całkowitego z " + c2.getClass().getSimpleName());
            
        }
        if (constant2 instanceof IntConstant) {
            return OBJECT_EQUAL.apply(constant1.c, c2);
        }
        throw new ResolvingException("Nie można porównać typu całkowitego z " + constant2.c.getClass().getSimpleName());
    }

    public static boolean check(StringConstant constant1, Constant<?> constant2) {
        if (constant2 instanceof StringConstant) {
            return OBJECT_EQUAL.apply(constant1.c, constant2.c);
        }
        throw new ResolvingException("Nie można porównać typu String z " + constant2.c.getClass().getSimpleName());
    }
}
