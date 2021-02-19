package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.ast.value.NullValue;
import com.server.parser.java.ast.value.PrimitiveValue;
import com.server.parser.java.ast.value.UninitializedValue;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.MethodContext;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VariableDefVisitorTest extends JavaVisitorTestBase {
    private final String METHOD_NAME = "methodName";
    private final MethodContext methodContext = createMethodContext(METHOD_NAME, "void");

    private final VariableDefVisitor visitor = new VariableDefVisitor();

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

        VariableDef variableDef = visitor.visit(c, context);

        assertThat(variableDef.getType()).isEqualTo(type);
        assertThat(variableDef.getName()).isEqualTo(name);
        assertThat(variableDef.getValue()).extracting(Value::toString).isEqualTo(value);
    }

    @Test
    void shouldVisitObjectFieldDecWithoutValue() {
        String input = "String a";
        JavaParser.FieldDecContext c = HELPER.shouldParseToEof(input, JavaParser::fieldDec);

        VariableDef variableDef = visitor.visit(c, context);

        assertThat(variableDef.getType()).isEqualTo("String");
        assertThat(variableDef.getName()).isEqualTo("a");
        assertThat(variableDef.getValue()).isExactlyInstanceOf(NullValue.class);
    }

    @Test
    void shouldVisitPrimitiveFieldDecWithoutValue() {
        String input = "int a";
        JavaParser.FieldDecContext c = HELPER.shouldParseToEof(input, JavaParser::fieldDec);

        VariableDef variableDef = visitor.visit(c, context);

        assertThat(variableDef.getType()).isEqualTo("int");
        assertThat(variableDef.getName()).isEqualTo("a");
        assertThat(variableDef.getValue()).isInstanceOf(PrimitiveValue.class);
        assertThat(((PrimitiveValue) variableDef.getValue()).getConstant().c).isEqualTo(0);
    }

    @Test
    void shouldVisitUninitializedVarDec() {
        String input = "int a";
        JavaParser.VarDecContext c = HELPER.shouldParseToEof(input, JavaParser::varDec);

        VariableDef variableDef = visitor.visit(c, context);

        assertThat(variableDef.getType()).isEqualTo("int");
        assertThat(variableDef.getName()).isEqualTo("a");
        assertThat(variableDef.getValue()).isInstanceOf(UninitializedValue.class);
    }

    @Test
    void shouldThrowWhenInvalidDeclarationFromLiteral() {
        String input = "String a = 's'";
        JavaParser.VarDecContext c = HELPER.shouldParseToEof(input, JavaParser::varDec);

        assertThatThrownBy(() -> visitor.visit(c, context))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Wyrażenie 's' nie jest typu String");
    }

    @Test
    void shouldVisitSingleMethodArg() {
        String input = "Integer[] a";
        JavaParser.SingleMethodArgContext c = HELPER.shouldParseToEof(input, JavaParser::singleMethodArg);

        VariableDef variableDef = visitor.visit(c, context);

        assertThat(variableDef.getText()).isEqualTo(input);
        assertVariableDec(variableDef, "Integer[]", "a");
    }

    @Test
    void shouldVisitLocalVarDec() {
        String input = "final String a = \"str\"";
        JavaParser.MethodVarDecContext c = HELPER.shouldParseToEof(input, JavaParser::methodVarDec);

        VariableDef variableDef = visitor.visit(c, methodContext);

        assertThat(variableDef.getText()).isEqualTo("String a = \"str\"");
        assertThat(variableDef.getResolved()).isEqualTo(input);
        assertVariableDec(variableDef, Collections.singletonList("final"), "String", "a");
        assertThat(variableDef.getValue()).extracting(Value::toString)
                .isEqualTo("\"str\"");
        assertThat(variableDef.printMethodName()).isEqualTo(METHOD_NAME);

        assertThat(methodContext.getNameToVariable().keySet()).containsExactly("a");
        Variable variable = methodContext.getNameToVariable().get("a");
        assertThat(Iterables.getOnlyElement(variable.getModifiers())).isEqualTo("final");
        assertThat(variable.getValue().toString()).isEqualTo("\"str\"");
    }

    @Test
    void shouldCreateFromFieldDec() {
        String input = "private static String a = \"str\"";
        JavaParser.FieldDecContext c = HELPER.shouldParseToEof(input, JavaParser::fieldDec);

        VariableDef variableDef = visitor.visit(c, context);

        assertThat(variableDef.getText()).isEqualTo("String a = \"str\"");
        assertThat(variableDef.getResolved()).isEqualTo(input);
        assertVariableDec(variableDef, Arrays.asList("private", "static"), "String", "a");
        assertThat(variableDef.getValue()).extracting(Value::toString)
                .isEqualTo("\"str\"");
    }
}