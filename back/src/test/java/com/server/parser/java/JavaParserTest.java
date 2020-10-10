package com.server.parser.java;

import com.server.parser.ParserTestHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    void shouldParseSingleMethodArg() {
        HELPER.shouldParseToEof("Integer[] a", JavaParser::singleMethodArg);
    }

    @Test
    void shouldParseMethodArgs() {
        HELPER.shouldParseToEof("(Integer[] a, double b)", JavaParser::methodArgs);
    }

    @Test
    void shouldParseMethodHeader() {
        HELPER.shouldParseToEof("void m(String[] a)", JavaParser::methodHeader);
    }

    @Test
    void shouldParseMethodDec() {
        HELPER.shouldParseToEof("void m(String[] a) { println(\"HELLO\"); }", JavaParser::methodDec);
    }

    @Test
    void shouldParseClassBody() {
        HELPER.shouldParseToEof("int i; void m(){} void m2(){} private String s;", JavaParser::classBody);
    }

    @Test
    void shouldParseClassDec() {
        HELPER.shouldParseToEof("public class c { void m() {} }", JavaParser::classDec);
    }

    @Test
    void shouldParseTask() {
        HELPER.shouldParseToEof("public class c {}", JavaParser::task);
    }

    @Test
    void shouldParseMethodName() {
        HELPER.shouldParseToEof("a.b", JavaParser::methodName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"abc\"", "\"a b\"", "\"a\\\"a\"", "'a'"})
    void shouldParseLiteral(String literal) {
        HELPER.shouldParseToEof(literal, JavaParser::literal);
    }

    @Test
    void shouldParseMethodCall() {
        HELPER.shouldParseToEof("System.out.print(\"Hello World\")", JavaParser::methodCall);
    }

    @Test
    void shouldParseFieldDec() {
        HELPER.shouldParseToEof("private final String a = \"str\"", JavaParser::fieldDec);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "String s = \"str\"",
            "char c = 'c'",
            "Character c = 'c'",
            "int i = 5",
            "Integer i = 5",
            "byte b = 5",
            "Byte b = 5"
    })
    void shouldParseVarDec(String input) {
        HELPER.shouldParseToEof(input, JavaParser::varDec);
    }

    @Test
    void shouldParseLocalVarDec() {
        HELPER.shouldParseToEof("final String a = \"str\"", JavaParser::localVarDec);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"abc\"", "a", "a.b", "'a'"})
    void shouldParseExpression(String literal) {
        HELPER.shouldParseToEof(literal, JavaParser::expression);
    }
}