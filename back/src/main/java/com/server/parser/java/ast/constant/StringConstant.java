package com.server.parser.java.ast.constant;

import java.util.Objects;

public class StringConstant extends TextConstant<String> {

    public StringConstant(String value) {
        super(Objects.requireNonNull(value, "value cannot be null"));
    }

    @Override
    public String toString() {
        return '"' + super.toString() + '"';
    }
}