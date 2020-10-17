package com.server.parser.java.ast.constant;

import java.util.Objects;

public class CharacterConstant extends TextConstant<Character> {

    public CharacterConstant(Character value) {
        super(Objects.requireNonNull(value, "value cannot be null"));
    }

    @Override
    public String toString() {
        return '\'' + super.toString() + '\'';
    }
}