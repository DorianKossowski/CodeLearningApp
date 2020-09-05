package com.server.parser.java;

import com.google.common.collect.Iterables;
import com.server.parser.ParserTestHelper;
import com.server.parser.java.ast.*;
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
    void shouldParseSingleMethodArg() {
        HELPER.shouldParseToEof("Integer[] a", JavaParser::singleMethodArg);
    }

    private void assertVariableDec(Variable variable, String type, String name) {
        assertThat(variable).extracting(Variable::getType, Variable::getName)
                .containsExactly(type, name);
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
    void shouldCreateFromMethodDec() {
        String input = "void m(String[] a) { println(\"HELLO\"); }";
        Method method = HELPER.shouldParseToEof(input, JavaParser::methodDec).method;

        MethodHeader header = method.getHeader();
        Statement statement = Iterables.getOnlyElement(method.getBody().getStatements());
        assertThat(header).extracting(MethodHeader::getResult, MethodHeader::getName)
                .containsExactly("void", "m");
        assertVariableDec(Iterables.getOnlyElement(header.getArguments()), "String[]", "a");
        assertThat(((MethodCall) statement)).extracting(MethodCall::getName,
                call -> Iterables.getOnlyElement(call.getArgs()).getText())
                .containsExactly("println", "HELLO");
    }

    @Test
    void shouldCreateFromClassBody() {
        String input = "int i; void m(){} void m2(){} private String s;";
        ClassBody body = HELPER.shouldParseToEof(input, JavaParser::classBody).body;

        assertThat(body.getMethods()).extracting(method -> method.getHeader().getName())
                .containsExactly("m", "m2");
        assertThat(body.getFields()).extracting(Variable::getName)
                .containsExactly("i", "s");
    }

    @Test
    void shouldCreateFromClassHeader() {
        String input = "public class c";
        ClassHeader header = HELPER.shouldParseToEof(input, JavaParser::classHeader).header;

        assertThat(header.getName()).isEqualTo("c");
    }

    @Test
    void shouldCreateFromClassDec() {
        String input = "public class c { void m() {} }";
        ClassAst classAst = HELPER.shouldParseToEof(input, JavaParser::classDec).classAst;

        assertThat(classAst.getHeader().getName()).isEqualTo("c");
        assertThat(Iterables.getOnlyElement(classAst.getBody().getMethods()).getHeader().getName()).isEqualTo("m");
    }

    @Test
    void shouldCreateFromExercise() {
        String input = "public class c {}";
        Exercise exercise = HELPER.shouldParseToEof(input, JavaParser::exercise).e;

        assertThat(exercise.getClassAst().getHeader().getName()).isEqualTo("c");
    }

    @Test
    void shouldParseMethodName() {
        String input = "a.b";
        String name = HELPER.shouldParseToEof(input, JavaParser::methodName).name;

        assertThat(name).isEqualTo(input);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\"abc\"", "\"a b\"", "\"a\\\"a\"", "'a'"})
    void shouldParseLiteral(String literal) {
        HELPER.shouldParseToEof(literal, JavaParser::literal);
    }

    @Test
    void shouldCreateFromMethodCall() {
        String input = "System.out.print(\"Hello World\");";
        MethodCall call = HELPER.shouldParseToEof(input, JavaParser::methodCall).mc;

        assertThat(call.getName()).isEqualTo("System.out.print");
        assertThat(Iterables.getOnlyElement(call.getArgs()).getText()).isEqualTo("Hello World");
    }

    @Test
    void shouldCreateFromFieldDec() {
        String input = "private final String a = \"str\";";
        Variable variable = HELPER.shouldParseToEof(input, JavaParser::fieldDec).v;

        assertVariableDec(variable, "String", "a");
        assertThat(variable.getValue()).extracting(Expression::getText)
                .isEqualTo("str");
    }

    @Test
    void shouldParseLocalVarDec() {
        HELPER.shouldParseToEof("final String a = \"str\";", JavaParser::localVarDec);
    }
}