package com.server.parser.util;

import com.server.parser.java.ast.constant.BooleanConstant;
import com.server.parser.java.ast.constant.CharacterConstant;
import com.server.parser.java.ast.constant.IntConstant;
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

        BooleanConstant result = EqualityOperatorService.check(constant1, constant2, type);

        assertThat(result.c).isEqualTo(expectedResult);
    }

    @Test
    void shouldCheckDifferentPrimitiveTypes() {
        IntConstant constant1 = new IntConstant(97);
        CharacterConstant constant2 = new CharacterConstant('a');

        BooleanConstant result = EqualityOperatorService.check(constant1, constant2, EqualityOperatorService.EqualityType.PRIMITIVE);

        assertThat(result.c).isEqualTo(true);
    }
}