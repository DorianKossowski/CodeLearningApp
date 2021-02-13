package com.server.parser.util;

import java.util.Arrays;

public enum ValueType {
    CHAR("char"),
    INT("int"),
    BYTE("byte"),
    SHORT("short"),
    LONG("long"),
    FLOAT("float"),
    DOUBLE("double"),
    BOOLEAN("boolean"),
    STRING("String"),
    CHARACTER("Character"),
    INTEGER("Integer"),
    BYTE_OBJ("Byte"),
    SHORT_OBJ("Short"),
    LONG_OBJ("Long"),
    FLOAT_OBJ("Float"),
    DOUBLE_OBJ("Double"),
    BOOLEAN_OBJ("Boolean");

    private final String type;

    ValueType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ValueType findByOriginalType(String type) {
        return Arrays.stream(ValueType.values())
                .filter(valueType -> valueType.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum for type " + type));
    }
}