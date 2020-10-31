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

class EqualityServiceTest {

    static Stream<Arguments> intIntOperationProvider() {
        return Stream.of(
                Arguments.of(EqualityService.EqualityType.PRIMITIVE, true),
                Arguments.of(EqualityService.EqualityType.OBJECT, false)
        );
    }

    @ParameterizedTest
    @MethodSource("intIntOperationProvider")
    void shouldCheckPrimitiveTypes(EqualityService.EqualityType type, boolean expectedResult) {
        IntConstant constant1 = new IntConstant(1000);
        IntConstant constant2 = new IntConstant(1000);

        BooleanConstant result = EqualityService.check(constant1, constant2, type);

        assertThat(result.c).isEqualTo(expectedResult);
    }

    @Test
    void shouldCheckDifferentPrimitiveTypes() {
        IntConstant constant1 = new IntConstant(97);
        CharacterConstant constant2 = new CharacterConstant('a');

        BooleanConstant result = EqualityService.check(constant1, constant2, EqualityService.EqualityType.PRIMITIVE);

        assertThat(result.c).isEqualTo(true);
    }
}