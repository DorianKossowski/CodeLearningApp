package com.server.parser.java.ast.constant;

import com.server.parser.util.EqualityService;

import java.util.Objects;

public class StringConstant extends TextConstant<String> {

    public StringConstant(String value) {
        super(Objects.requireNonNull(value, "value cannot be null"));
    }

    @Override
    public String toString() {
        return '"' + super.toString() + '"';
    }

    @Override
    public BooleanConstant equalsC(Constant<?> constant2, EqualityService.EqualityType type) {
        return EqualityService.check(this, constant2, type);
    }
}