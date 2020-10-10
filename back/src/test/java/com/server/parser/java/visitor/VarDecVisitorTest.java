package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Variable;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VarDecVisitorTest extends JavaVisitorTestBase {
    private final VarDecVisitor visitor = new VarDecVisitor();

    static Stream<Arguments> decWithLiteralsProvider() {
        return Stream.of(
                Arguments.of("String s = \"str\"", "String", "s", "\"str\""),
                Arguments.of("char c = 'c'", "char", "c", "'c'"),
                Arguments.of("Character c = 'c'", "Character", "c", "'c'"),
                Arguments.of("int i = 5", "int", "i", "5"),
                Arguments.of("Integer i = 5", "Integer", "i", "5"),
                Arguments.of("byte b = 5", "byte", "b", "5"),
                Arguments.of("Byte b = 5", "Byte", "b", "5"),
                Arguments.of("short s = 5", "short", "s", "5"),
                Arguments.of("Short s = 5", "Short", "s", "5"),
                Arguments.of("long l = 5l", "long", "l", "5"),
                Arguments.of("Long l = 5L", "Long", "l", "5"),
                Arguments.of("float f = 5.f", "float", "f", "5.0"),
                Arguments.of("Float f = 5.5F", "Float", "f", "5.5"),
                Arguments.of("double d = 5.d", "double", "d", "5.0"),
                Arguments.of("Double d = .5D", "Double", "d", "0.5")
        );
    }

    @ParameterizedTest
    @MethodSource("decWithLiteralsProvider")
    void shouldVisitVarDecWithLiteral(String input, String type, String name, String value) {
        JavaParser.VarDecContext c = HELPER.shouldParseToEof(input, JavaParser::varDec);

        Variable variable = visitor.visit(c);

        assertThat(variable.getType()).isEqualTo(type);
        assertThat(variable.getName()).isEqualTo(name);
        assertThat(variable.getValue()).extracting(Expression::getText).isEqualTo(value);
    }

    @Test
    void shouldVisitVarDecWithoutValue() {
        String input = "String a";
        JavaParser.VarDecContext c = HELPER.shouldParseToEof(input, JavaParser::varDec);

        Variable variable = visitor.visit(c);

        assertThat(variable.getType()).isEqualTo("String");
        assertThat(variable.getName()).isEqualTo("a");
        assertThat(variable.getValue()).isNull();
    }

    @Test
    void shouldThrowWhenInvalidDeclarationFromLiteral() {
        String input = "String a = 's'";
        JavaParser.VarDecContext c = HELPER.shouldParseToEof(input, JavaParser::varDec);

        assertThatThrownBy(() -> visitor.visit(c))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Wyrażenie 's' nie jest typu String");
    }
}