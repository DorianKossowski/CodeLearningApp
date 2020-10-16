package com.server.parser.util;

import com.server.parser.java.ast.constant.DoubleConstant;
import com.server.parser.java.ast.constant.IntConstant;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MathOperationServiceTest {

    static Stream<Arguments> intIntOperationProvider() {
        return Stream.of(
                Arguments.of(new IntConstant(1), new IntConstant(2), "+", new IntConstant(3)),
                Arguments.of(new IntConstant(1), new IntConstant(2), "-", new IntConstant(-1)),
                Arguments.of(new IntConstant(1), new IntConstant(2), "*", new IntConstant(2)),
                Arguments.of(new IntConstant(1), new IntConstant(2), "/", new IntConstant(0))
        );
    }

    @ParameterizedTest
    @MethodSource("intIntOperationProvider")
    void shouldComputeIntInt(IntConstant c1, IntConstant c2, String operator, IntConstant expectedResult) {
        IntConstant result = MathOperationService.compute(c1, c2, operator);
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void shouldThrowWhenDivBy0() {
        assertThatThrownBy(() -> MathOperationService.compute(new IntConstant(1), new IntConstant(0), "/"))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Dzielenie przez 0 jest niedozwolone");
    }

    static Stream<Arguments> intDoubleOperationProvider() {
        return Stream.of(
                Arguments.of(new IntConstant(1), new DoubleConstant(2.0), "+", new DoubleConstant(3.0)),
                Arguments.of(new IntConstant(1), new DoubleConstant(2.0), "-", new DoubleConstant(-1.0)),
                Arguments.of(new IntConstant(1), new DoubleConstant(2.0), "*", new DoubleConstant(2.0)),
                Arguments.of(new IntConstant(1), new DoubleConstant(2.0), "/", new DoubleConstant(0.5))
        );
    }

    @ParameterizedTest
    @MethodSource("intDoubleOperationProvider")
    void shouldComputeIntDouble(IntConstant c1, DoubleConstant c2, String operator, DoubleConstant expectedResult) {
        DoubleConstant result = MathOperationService.compute(c1, c2, operator);
        assertThat(result).isEqualTo(expectedResult);
    }

    static Stream<Arguments> doubleIntOperationProvider() {
        return Stream.of(
                Arguments.of(new DoubleConstant(1.0), new IntConstant(2), "+", new DoubleConstant(3.0)),
                Arguments.of(new DoubleConstant(1.0), new IntConstant(2), "-", new DoubleConstant(-1.0)),
                Arguments.of(new DoubleConstant(1.0), new IntConstant(2), "*", new DoubleConstant(2.0)),
                Arguments.of(new DoubleConstant(1.0), new IntConstant(2), "/", new DoubleConstant(0.5))
        );
    }

    @ParameterizedTest
    @MethodSource("doubleIntOperationProvider")
    void shouldComputeDoubleInt(DoubleConstant c1, IntConstant c2, String operator, DoubleConstant expectedResult) {
        DoubleConstant result = MathOperationService.compute(c1, c2, operator);
        assertThat(result).isEqualTo(expectedResult);
    }

    static Stream<Arguments> doubleDoubleOperationProvider() {
        return Stream.of(
                Arguments.of(new DoubleConstant(1.0), new DoubleConstant(2.0), "+", new DoubleConstant(3.0)),
                Arguments.of(new DoubleConstant(1.0), new DoubleConstant(2.0), "-", new DoubleConstant(-1.0)),
                Arguments.of(new DoubleConstant(1.0), new DoubleConstant(2.0), "*", new DoubleConstant(2.0)),
                Arguments.of(new DoubleConstant(1.0), new DoubleConstant(2.0), "/", new DoubleConstant(0.5))
        );
    }

    @ParameterizedTest
    @MethodSource("doubleDoubleOperationProvider")
    void shouldComputeDoubleDouble(DoubleConstant c1, DoubleConstant c2, String operator, DoubleConstant expectedResult) {
        DoubleConstant result = MathOperationService.compute(c1, c2, operator);
        assertThat(result).isEqualTo(expectedResult);
    }
}