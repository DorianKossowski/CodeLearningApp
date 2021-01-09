package com.server.parser.java.ast;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class MethodHeaderTest {

    static Stream<Arguments> modifiersProvider() {
        return Stream.of(
                Arguments.of(Collections.emptyList(), false),
                Arguments.of(Collections.singletonList("static"), true)
        );
    }

    @ParameterizedTest
    @MethodSource("modifiersProvider")
    void shouldCheckIsStatic(List<String> modifiers, boolean isStatic) {
        MethodHeader methodHeader = new MethodHeader(modifiers, "", "", Collections.emptyList());

        assertThat(methodHeader.isStatic()).isEqualTo(isStatic);
    }
}