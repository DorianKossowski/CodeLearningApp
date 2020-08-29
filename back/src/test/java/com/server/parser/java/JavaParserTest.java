package com.server.parser.java;

import com.google.common.collect.Iterables;
import com.server.parser.ParserTestHelper;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.Variable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaParserTest {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    @ParameterizedTest
    @ValueSource(strings = {"_", "$", "a", "$a1", "A1_"})
    void shouldParseIdentifier(String input) {
        HELPER.shouldParseToEof(input, JavaParser::identifier);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "class C",
            "abstract public class C",
            "static private class C"
    })
    void shouldParseClassHeader(String input) {
        HELPER.shouldParseToEof(input, JavaParser::classHeader);
    }

    @Test
    void shouldParseModifiers() {
        List<String> classModifiers = Arrays.asList("public", "protected", "private", "abstract", "static", "final",
                "strictfp");
        List<String> methodModifiers = Stream.concat(classModifiers.stream(), Stream.of("synchronized", "native"))
                .collect(Collectors.toList());

        classModifiers.forEach(modifier -> HELPER.shouldParseToEof(modifier, JavaParser::classModifier));
        methodModifiers.forEach(modifier -> HELPER.shouldParseToEof(modifier, JavaParser::methodModifier));
    }

    @ParameterizedTest
    @ValueSource(strings = {"short", "int", "long", "float", "double", "byte", "char", "boolean"})
    void shouldParsePrimitiveType(String input) {
        HELPER.shouldParseToEof(input, JavaParser::primitiveType);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Integer", "String", "MyClass"})
    void shouldParseObjectType(String input) {
        HELPER.shouldParseToEof(input, JavaParser::objectType);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Integer []", "int[]", "MyClass[]"})
    void shouldParseArrayType(String input) {
        HELPER.shouldParseToEof(input, JavaParser::arrayType);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "public static void m(String[] args)",
            "abstract String m()",
            "private final int m(Integer i, Double d)"
    })
    void shouldParseMethodHeader(String input) {
        HELPER.shouldParseToEof(input, JavaParser::methodHeader);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "int a", "int a, String[] b"})
    void shouldParseMethodArgs(String input) {
        HELPER.shouldParseToEof("(" + input + ")", JavaParser::methodArgs);
    }

    @Test
    void shouldCreateFromSingleMethodArg() {
        String input = "Integer[] a";
        Variable variable = HELPER.shouldParseToEof(input, JavaParser::singleMethodArg).var;

        assertVariable(variable, "Integer[]", "a");
    }

    private void assertVariable(Variable variable, String type, String name) {
        assertThat(variable).extracting(Variable::getType, Variable::getName)
                .containsExactly(type, name);
    }

    @Test
    void shouldCreateFromMethodArgs() {
        String input = "(Integer[] a, double b)";
        List<Variable> variables = HELPER.shouldParseToEof(input, JavaParser::methodArgs).args;

        assertThat(variables).hasSize(2);
        assertVariable(variables.get(0), "Integer[]", "a");
        assertVariable(variables.get(1), "double", "b");
    }

    @Test
    void shouldCreateFromMethodHeader() {
        String input = "void m(String[] a)";
        MethodHeader header = HELPER.shouldParseToEof(input, JavaParser::methodHeader).header;

        assertThat(header.getResult()).isEqualTo("void");
        assertThat(header.getName()).isEqualTo("m");
        assertVariable(Iterables.getOnlyElement(header.getArguments()), "String[]", "a");
    }
}