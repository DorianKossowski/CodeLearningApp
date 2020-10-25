package com.server.parser.java.ast.constant;

import com.server.parser.util.EqualityService;
import com.server.parser.util.NumberOperationService;

import java.util.Objects;

public class DoubleConstant extends Constant<Double> {

    public DoubleConstant(Double value) {
        super(Objects.requireNonNull(value, "value cannot be null"));
    }

    @Override
    protected Constant<?> compute(IntConstant c2, String operation) {
        return NumberOperationService.compute(this, c2, operation);
    }

    @Override
    protected Constant<?> compute(DoubleConstant c2, String operation) {
        return NumberOperationService.compute(this, c2, operation);
    }

    @Override
    public BooleanConstant equalsC(Constant<?> constant2, EqualityService.EqualityType type) {
        return EqualityService.check(this, constant2, type);
    }
}