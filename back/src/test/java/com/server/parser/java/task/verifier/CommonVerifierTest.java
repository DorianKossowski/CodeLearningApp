package com.server.parser.java.task.verifier;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CommonVerifierTest {
    private static final String MOD1 = "MOD1";
    private static final String MOD2 = "MOD2";

    static Stream<Arguments> modifiersProvider() {
        return Stream.of(
                Arguments.of(Collections.emptyList(), Collections.emptyList(), true),
                Arguments.of(Collections.singletonList(MOD1), Collections.emptyList(), false),
                Arguments.of(Arrays.asList(MOD1, MOD2), Collections.singletonList(MOD1), true),
                Arguments.of(Collections.singletonList(MOD1), Arrays.asList(MOD1, MOD2), false)
        );
    }

    @ParameterizedTest
    @MethodSource("modifiersProvider")
    void shouldCheckModifiers(List<String> actualModifiers, List<String> expectedModifiers, boolean areSame) {
        assertThat(CommonVerifier.hasSameModifiers(actualModifiers, expectedModifiers)).isEqualTo(areSame);
    }
}