package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.PrimitiveValue;
import com.server.parser.java.ast.Value;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.statement.Assignment;
import com.server.parser.java.ast.statement.MethodCall;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.context.MethodContext;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StatementVisitorTest extends JavaVisitorTestBase {
    private final String METHOD_NAME = "methodName";
    private final StatementVisitor visitor = new StatementVisitor();

    private MethodContext methodContext;

    @BeforeEach
    void setUp() {
        methodContext = context.createCurrentMethodContext(METHOD_NAME);
    }

    //*** METHOD CALL ***//
    @Test
    void shouldVisitMethodCall() {
        String input = "System.out.print(\"Hello World\")";
        JavaParser.MethodCallContext c = HELPER.shouldParseToEof(input, JavaParser::methodCall);

        MethodCall methodCall = (MethodCall) visitor.visit(c, context);

        assertThat(methodCall.getText()).isEqualTo(input);
        assertThat(methodCall.printMethodName()).isEqualTo(METHOD_NAME);
        assertThat(methodCall.getName()).isEqualTo("System.out.print");
        assertThat(Iterables.getOnlyElement(methodCall.getArgs()).getText()).isEqualTo("\"Hello World\"");
    }

    @Test
    void shouldGetCorrectMethodCallValue() {
        methodContext.addVar(createVariable("var"));
        String input = "System.out.print(\"literal\", var)";
        JavaParser.MethodCallContext c = HELPER.shouldParseToEof(input, JavaParser::methodCall);

        MethodCall methodCall = (MethodCall) visitor.visit(c, context);

        assertThat(methodCall.getResolved()).isEqualTo("System.out.print(\"literal\", \"value\")");
    }

    private Variable createVariable(String name) {
        StringConstant stringConstant = new StringConstant("value");
        PrimitiveValue value = new PrimitiveValue(new Literal(stringConstant));
        return new Variable("String", name, value);
    }

    //*** VARIABLE ***//
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

        VariableDef variableDef = (VariableDef) visitor.visit(c, context);

        assertThat(variableDef.getType()).isEqualTo(type);
        assertThat(variableDef.getName()).isEqualTo(name);
        assertThat(variableDef.getValue()).extracting(Value::getResolved).isEqualTo(value);
    }

    @Test
    void shouldVisitVarDecWithoutValue() {
        String input = "String a";
        JavaParser.VarDecContext c = HELPER.shouldParseToEof(input, JavaParser::varDec);

        VariableDef variableDef = (VariableDef) visitor.visit(c, context);

        assertThat(variableDef.getType()).isEqualTo("String");
        assertThat(variableDef.getName()).isEqualTo("a");
        assertThat(variableDef.getValue()).isNull();
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

        VariableDef variableDef = (VariableDef) visitor.visit(c, context);

        assertThat(variableDef.getText()).isEqualTo(input);
        assertVariableDec(variableDef, "Integer[]", "a");
    }

    @Test
    void shouldVisitLocalVarDec() {
        String input = "String a = \"str\"";
        JavaParser.MethodVarDecContext c = HELPER.shouldParseToEof(input, JavaParser::methodVarDec);

        VariableDef variableDef = (VariableDef) visitor.visit(c, context);

        assertThat(variableDef.getText()).isEqualTo(input);
        assertVariableDec(variableDef, "String", "a");
        assertThat(variableDef.getValue()).extracting(Value::getResolved)
                .isEqualTo("\"str\"");

        assertThat(methodContext.getNameToVariable().keySet()).containsExactly("a");
        assertThat(methodContext.getNameToVariable().get("a").getValue().getResolved()).isEqualTo("\"str\"");
    }

    @Test
    void shouldCreateFromFieldDec() {
        String input = "String a = \"str\"";
        JavaParser.FieldDecContext c = HELPER.shouldParseToEof(input, JavaParser::fieldDec);

        VariableDef variableDef = (VariableDef) visitor.visit(c, context);

        assertThat(variableDef.getText()).isEqualTo(input);
        assertVariableDec(variableDef, "String", "a");
        assertThat(variableDef.getValue()).extracting(Value::getResolved)
                .isEqualTo("\"str\"");
    }

    //*** ASSIGNMENT ***//
    @Test
    void shouldVisitAssignment() {
        methodContext.addVar(createVariable("a"));
        String input = "a = \"str\"";
        JavaParser.AssignmentContext c = HELPER.shouldParseToEof(input, JavaParser::assignment);

        Assignment assignment = (Assignment) visitor.visit(c, context);

        assertThat(assignment.getText()).isEqualTo(input);
        assertThat(assignment.getId()).isEqualTo("a");
        assertThat(assignment.getValue().getText()).isEqualTo("\"str\"");
    }
}