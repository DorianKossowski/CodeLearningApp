package com.server.parser.java.constant.util;

import com.server.parser.java.constant.CharacterConstant;
import com.server.parser.java.constant.IntConstant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class EqualityOperatorServiceTest {

    static Stream<Arguments> intIntOperationProvider() {
        return Stream.of(
                Arguments.of(EqualityOperatorService.EqualityType.PRIMITIVE, true),
                Arguments.of(EqualityOperatorService.EqualityType.OBJECT, false)
        );
    }

    @ParameterizedTest
    @MethodSource("intIntOperationProvider")
    void shouldCheckPrimitiveTypes(EqualityOperatorService.EqualityType type, boolean expectedResult) {
        IntConstant constant1 = new IntConstant(1000);
        IntConstant constant2 = new IntConstant(1000);

        boolean result = EqualityOperatorService.check(constant1, constant2, type);

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldCheckDifferentPrimitiveTypes() {
        IntConstant constant1 = new IntConstant(97);
        CharacterConstant constant2 = new CharacterConstant('a');

        boolean result = EqualityOperatorService.check(constant1, constant2, EqualityOperatorService.EqualityType.PRIMITIVE);

        assertThat(result).isEqualTo(true);
    }
}