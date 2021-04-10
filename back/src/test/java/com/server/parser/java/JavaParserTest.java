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
    @ValueSource(strings = {
            "public m(String[] args)", "m()", "private m(Integer i, Double d)", "protected m(int x)"
    })
    void shouldParseConstructorHeader(String input) {
        HELPER.shouldParseToEof(input, JavaParser::constructorHeader);
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
    void shouldParseConstructorDec() {
        HELPER.shouldParseToEof("m(String[] a) { println(\"HELLO\"); }", JavaParser::constructorDec);
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

    @ParameterizedTest
    @ValueSource(strings = {"fun()", "a.fun()", "a.b.fun()"})
    void shouldParseMethodCall(String input) {
        HELPER.shouldParseToEof(input, JavaParser::call);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"abc\"", "\"a b\"", "\"a\\\"a\"", "'a'"})
    void shouldParseLiteral(String literal) {
        HELPER.shouldParseToEof(literal, JavaParser::literal);
    }

    @Test
    void shouldParsePrintMethodCall() {
        HELPER.shouldParseToEof("System.out.print(\"Hello World\")", JavaParser::call);
    }

    @Test
    void shouldParseFieldDec() {
        HELPER.shouldParseToEof("private final String a = \"str\"", JavaParser::fieldDec);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "String s = \"str\"",
            "char c = 'c'", "Character c = 'c'",
            "int i = 5", "Integer i = 5",
            "byte b = 5", "Byte b = 5",
            "long l = 5l", "Long l = 5L",
            "float f = 5.5", "float f = 5f", "Float f = 5.F",
            "double d = 5.5d", "Double d = .5D",
            "boolean b = false", "Double d = true"
    })
    void shouldParseVarDec(String input) {
        HELPER.shouldParseToEof(input, JavaParser::varDec);
    }

    @Test
    void shouldParseMethodVarDec() {
        HELPER.shouldParseToEof("final String a = \"str\"", JavaParser::methodVarDec);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"abc\"", "a", "a.b", "'a'",
            "(a+b)", "1 * a", "1 / a", "-5", "1 % a",
            "a>b", "a>=b", "a<b", "a<=b", "a==b", "a!=b",
            "f()", "a.f(1)", "f(1,2)",
            "a&&b", "a||b",
            "null"
    })
    void shouldParseExpression(String literal) {
        HELPER.shouldParseToEof(literal, JavaParser::expression);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "s = \"str\"",
            "s = s0",
            "s1.s2 = s3"
    })
    void shouldParseAssignment(String input) {
        HELPER.shouldParseToEof(input, JavaParser::assignment);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "if(true);", "if(true) fun();", "if(a == 1) { }", "if(true) { fun(); }", "if(true) { fun(); fun(); }",
            "if(false) {} else fun();", "if(false) {} else { fun(); fun(); }",
    })
    void shouldParseIfStmt(String input) {
        HELPER.shouldParseToEof(input, JavaParser::ifElseStatement);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{ }", "{ fun(); }", "{ fun1(); fun2(); }"
    })
    void shouldParseBlockStmt(String input) {
        HELPER.shouldParseToEof(input, JavaParser::blockStatement);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "switch(cond) { }", "switch(cond) { case x: fun(); }", "switch(cond) { case x:case y: fun(); fun(); }",
            "switch(cond) { case x: fun(); case y: fun(); case z: }", "switch(cond) { default: fun(); }"
    })
    void shouldParseSwitchStmt(String input) {
        HELPER.shouldParseToEof(input, JavaParser::switchStatement);
    }

    @Test
    void shouldParseBreak() {
        HELPER.shouldParseToEof("break", JavaParser::breakStatement);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "for(;;) fun();", "for(int x = 0; ;);", "for(x=0; x<1; ) {}", "for(; x<1; x = x+1) { fun(); fun(); }"
    })
    void shouldParseForStmt(String input) {
        HELPER.shouldParseToEof(input, JavaParser::forStatement);
    }

    @Test
    void shouldParseWhileStmt() {
        HELPER.shouldParseToEof("while (true) { fun(); }", JavaParser::whileStatement);
    }

    @Test
    void shouldParseDoWhileStmt() {
        HELPER.shouldParseToEof("do { fun(); } while (true);", JavaParser::doWhileStatement);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "return", "return x"
    })
    void shouldParseReturn(String input) {
        HELPER.shouldParseToEof(input, JavaParser::returnStatement);
    }

    @Test
    void shouldParseConstructorCall() {
        HELPER.shouldParseToEof("new MyClass(10)", JavaParser::call);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "a", "a.b", "a.b.c"
    })
    void shouldParseObjectRefName(String input) {
        HELPER.shouldParseToEof(input, JavaParser::objectRefName);
    }
}