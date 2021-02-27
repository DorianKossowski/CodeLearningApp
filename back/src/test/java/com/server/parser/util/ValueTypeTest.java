package com.server.parser.util;

import org.junit.jupiter.api.Test;

import static com.server.parser.util.ValueType.ARRAY;
import static com.server.parser.util.ValueType.GENERIC;
import static org.assertj.core.api.Assertions.assertThat;

class ValueTypeTest {

    @Test
    void shouldReturnGeneric() {
        ValueType type = ValueType.findByOriginalType("SomeType");

        assertThat(type).isSameAs(GENERIC);
        assertThat(type)
                .returns("GenericType", ValueType::getType)
                .returns(true, ValueType::isObjectType);
    }

    @Test
    void shouldReturnArray() {
        ValueType type = ValueType.findByOriginalType("int[]");

        assertThat(type).isSameAs(ARRAY);
        assertThat(type)
                .returns("ArrayType", ValueType::getType)
                .returns(true, ValueType::isObjectType);
    }
}