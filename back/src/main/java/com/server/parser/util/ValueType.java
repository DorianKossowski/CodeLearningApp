package com.server.parser.util;

import java.util.Arrays;

public enum ValueType {
    CHAR("char", false),
    INT("int", false),
    BYTE("byte", false),
    SHORT("short", false),
    LONG("long", false),
    FLOAT("float", false),
    DOUBLE("double", false),
    BOOLEAN("boolean", false),
    STRING("String", true),
    CHARACTER("Character", true),
    INTEGER("Integer", true),
    BYTE_OBJ("Byte", true),
    SHORT_OBJ("Short", true),
    LONG_OBJ("Long", true),
    FLOAT_OBJ("Float", true),
    DOUBLE_OBJ("Double", true),
    BOOLEAN_OBJ("Boolean", true),
    GENERIC("GenericType", true);

    private final String type;
    private final boolean isObjectType;

    ValueType(String type, boolean isObjectType) {
        this.type = type;
        this.isObjectType = isObjectType;
    }

    public String getType() {
        return type;
    }

    public boolean isObjectType() {
        return isObjectType;
    }

    public static ValueType findByOriginalType(String type) {
        return Arrays.stream(ValueType.values())
                .filter(valueType -> valueType.getType().equals(type))
                .findFirst()
                .orElse(GENERIC);
    }
}