package com.server.parser.java.constant;

import com.server.parser.util.EqualityOperatorService;
import com.server.parser.util.NumberOperationService;

import java.util.Objects;

public class DoubleConstant extends Constant<Double> {

    public DoubleConstant() {
        this(new Double(0.0));
    }

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
    public boolean equalsC(Constant<?> constant2, EqualityOperatorService.EqualityType type) {
        return EqualityOperatorService.check(this, constant2, type);
    }
}