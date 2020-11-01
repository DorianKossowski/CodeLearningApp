package com.server.parser.java.ast.constant;

import com.server.parser.util.EqualityOperatorService;
import com.server.parser.util.NumberOperationService;

import java.util.Objects;

public class CharacterConstant extends Constant<Character> {

    public CharacterConstant(Character value) {
        super(Objects.requireNonNull(value, "value cannot be null"));
    }

    @Override
    protected Constant<?> compute(IntConstant c2, String operation) {
        return NumberOperationService.compute(new IntConstant((int) c), c2, operation);
    }

    @Override
    protected Constant<?> compute(DoubleConstant c2, String operation) {
        return NumberOperationService.compute(new DoubleConstant((double) c), c2, operation);
    }

    @Override
    public String toString() {
        return '\'' + super.toString() + '\'';
    }

    @Override
    public boolean equalsC(Constant<?> constant2, EqualityOperatorService.EqualityType type) {
        return EqualityOperatorService.check(this, constant2, type);
    }
}