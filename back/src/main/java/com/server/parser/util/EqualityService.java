package com.server.parser.util;

import com.server.parser.java.ast.constant.*;
import com.server.parser.util.exception.ResolvingException;

import java.util.*;
import java.util.function.BiFunction;

// ONLY == operator
public class EqualityService {
    public enum EqualityType {
        PRIMITIVE, OBJECT
    }

    private static final Map<EqualityType, BiFunction<Object, Object, Boolean>> typeToOperation = new HashMap<>();

    static {
        typeToOperation.put(EqualityType.PRIMITIVE, Object::equals);
        typeToOperation.put(EqualityType.OBJECT, (c1, c2) -> c1 == c2);
    }

    public static BooleanConstant check(BooleanConstant constant1, Constant<?> constant2, EqualityType type) {
        if (isValidChecking(constant2, Collections.singletonList(BooleanConstant.class))) {
            return new BooleanConstant(typeToOperation.get(type).apply(constant1.c, constant2.c));
        }
        throw new ResolvingException("Nie można porównać typu logicznego z " + constant2.c.getClass().getSimpleName());
    }

    public static BooleanConstant check(CharacterConstant constant1, Constant<?> constant2, EqualityType type) {
        if (isValidChecking(constant2, Arrays.asList(CharacterConstant.class, IntConstant.class, DoubleConstant.class))) {
            return new BooleanConstant(typeToOperation.get(type).apply(constant1.c, constant2.c));
        }
        throw new ResolvingException("Nie można porównać typu znakowego z " + constant2.c.getClass().getSimpleName());
    }

    private static boolean isValidChecking(Constant<?> constant2, List<Class<? extends Constant<?>>> possibleConstantTypes) {
        for (Class<? extends Constant<?>> type : possibleConstantTypes) {
            if (constant2.getClass() == type) {
                return true;
            }
        }
        return false;
    }

    public static BooleanConstant check(DoubleConstant constant1, Constant<?> constant2, EqualityType type) {
        if (isValidChecking(constant2, Arrays.asList(DoubleConstant.class, IntConstant.class, CharacterConstant.class))) {
            return new BooleanConstant(typeToOperation.get(type).apply(constant1.c, constant2.c));
        }
        throw new ResolvingException("Nie można porównać typu dziesiętnego z " + constant2.c.getClass().getSimpleName());
    }

    public static BooleanConstant check(IntConstant constant1, Constant<?> constant2, EqualityType type) {
        if (isValidChecking(constant2, Arrays.asList(IntConstant.class, DoubleConstant.class, CharacterConstant.class))) {
            return new BooleanConstant(typeToOperation.get(type).apply(constant1.c, constant2.c));
        }
        throw new ResolvingException("Nie można porównać typu całkowitego z " + constant2.c.getClass().getSimpleName());
    }

    public static BooleanConstant check(StringConstant constant1, Constant<?> constant2, EqualityType type) {
        if (isValidChecking(constant2, Collections.singletonList(StringConstant.class))) {
            return new BooleanConstant(typeToOperation.get(type).apply(constant1.c, constant2.c));
        }
        throw new ResolvingException("Nie można porównać typu String z " + constant2.c.getClass().getSimpleName());
    }
}
