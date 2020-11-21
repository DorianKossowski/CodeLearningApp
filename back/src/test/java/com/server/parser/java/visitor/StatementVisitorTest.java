package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.statement.*;
import com.server.parser.java.ast.value.NullValue;
import com.server.parser.java.ast.value.PrimitiveValue;
import com.server.parser.java.ast.value.UninitializedValue;
import com.server.parser.java.ast.value.Value;
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

    @Test
    void shouldVisitBlockStatement() {
        String input = "{ int a = 1; boolean b = false; }";
        JavaParser.BlockStatementContext c = HELPER.shouldParseToEof(input, JavaParser::blockStatement);

        BlockStatement statement = (BlockStatement) visitor.visit(c, context);

        assertThat(statement.getText()).isEqualTo("{ BLOCK STATEMENT }");
        assertThat(statement.getExpressionStatements()).extracting(Statement::getText)
                .containsExactly("int a = 1", "boolean b = false");
    }

    //*** METHOD CALL ***//
    @Test
    void shouldVisitMethodCall() {
        String input = "System.out.print(\"Hello World\")";
        JavaParser.CallContext c = HELPER.shouldParseToEof(input, JavaParser::call);

        MethodCall methodCall = (MethodCall) visitor.visit(c, context);

        assertThat(methodCall.getText()).isEqualTo(input);
        assertThat(methodCall.printMethodName()).isEqualTo(METHOD_NAME);
        assertThat(methodCall.getName()).isEqualTo("System.out.print");
        assertThat(Iterables.getOnlyElement(methodCall.getArgs()).getText()).isEqualTo("\"Hello World\"");
    }

    @Test
    void shouldVisitMethodCallWithoutArgs() {
        String input = "System.out.print()";
        JavaParser.CallContext c = HELPER.shouldParseToEof(input, JavaParser::call);

        MethodCall methodCall = (MethodCall) visitor.visit(c, context);

        assertThat(methodCall.getText()).isEqualTo(input);
        assertThat(methodCall.printMethodName()).isEqualTo(METHOD_NAME);
        assertThat(methodCall.getName()).isEqualTo("System.out.print");
        assertThat(methodCall.getArgs()).isEmpty();
    }

    @Test
    void shouldGetCorrectMethodCallValue() {
        methodContext.addVar(createStringVariable("var"));
        String input = "System.out.print(\"literal\", var)";
        JavaParser.CallContext c = HELPER.shouldParseToEof(input, JavaParser::call);

        MethodCall methodCall = (MethodCall) visitor.visit(c, context);

        assertThat(methodCall.getResolved()).isEqualTo("System.out.print(\"literal\", \"value\")");
    }

    private Variable createStringVariable(String name) {
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
        assertThat(variableDef.getValue()).extracting(Value::toString).isEqualTo(value);
    }

    @Test
    void shouldVisitObjectFieldDecWithoutValue() {
        String input = "String a";
        JavaParser.FieldDecContext c = HELPER.shouldParseToEof(input, JavaParser::fieldDec);

        VariableDef variableDef = (VariableDef) visitor.visit(c, context);

        assertThat(variableDef.getType()).isEqualTo("String");
        assertThat(variableDef.getName()).isEqualTo("a");
        assertThat(variableDef.getValue()).isExactlyInstanceOf(NullValue.class);
    }

    @Test
    void shouldVisitPrimitiveFieldDecWithoutValue() {
        String input = "int a";
        JavaParser.FieldDecContext c = HELPER.shouldParseToEof(input, JavaParser::fieldDec);

        VariableDef variableDef = (VariableDef) visitor.visit(c, context);

        assertThat(variableDef.getType()).isEqualTo("int");
        assertThat(variableDef.getName()).isEqualTo("a");
        assertThat(variableDef.getValue()).isInstanceOf(PrimitiveValue.class);
        assertThat(((PrimitiveValue) variableDef.getValue()).getConstant().c).isEqualTo(0);
    }

    @Test
    void shouldVisitUninitializedVarDec() {
        String input = "int a";
        JavaParser.VarDecContext c = HELPER.shouldParseToEof(input, JavaParser::varDec);

        VariableDef variableDef = (VariableDef) visitor.visit(c, context);

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
        assertThat(variableDef.getValue()).extracting(Value::toString)
                .isEqualTo("\"str\"");

        assertThat(methodContext.getNameToVariable().keySet()).containsExactly("a");
        assertThat(methodContext.getNameToVariable().get("a").getValue().toString()).isEqualTo("\"str\"");
    }

    @Test
    void shouldCreateFromFieldDec() {
        String input = "String a = \"str\"";
        JavaParser.FieldDecContext c = HELPER.shouldParseToEof(input, JavaParser::fieldDec);

        VariableDef variableDef = (VariableDef) visitor.visit(c, context);

        assertThat(variableDef.getText()).isEqualTo(input);
        assertVariableDec(variableDef, "String", "a");
        assertThat(variableDef.getValue()).extracting(Value::toString)
                .isEqualTo("\"str\"");
    }

    //*** ASSIGNMENT ***//
    @Test
    void shouldVisitAssignment() {
        methodContext.addVar(createStringVariable("a"));
        String input = "a = \"str\"";
        JavaParser.AssignmentContext c = HELPER.shouldParseToEof(input, JavaParser::assignment);

        Assignment assignment = (Assignment) visitor.visit(c, context);

        assertThat(assignment.getText()).isEqualTo(input);
        assertThat(assignment.getId()).isEqualTo("a");
        assertThat(assignment.getValue().getText()).isEqualTo("\"str\"");
    }

    @Test
    void shouldThrowWhenInvalidAssignment() {
        methodContext.addVar(createStringVariable("a"));
        String input = "a = 5";
        JavaParser.AssignmentContext c = HELPER.shouldParseToEof(input, JavaParser::assignment);

        assertThatThrownBy(() -> visitor.visit(c, context))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Wyrażenie 5 nie jest typu String");
    }

    //*** IF ***//
    @Test
    void shouldVisitIf() {
        String input = "if(true) { System.out.print(1); if(true) { System.out.print(2); } }";
        JavaParser.IfElseStatementContext c = HELPER.shouldParseToEof(input, JavaParser::ifElseStatement);

        IfElseStatement ifElseStatement = (IfElseStatement) visitor.visit(c, context);

        assertThat(ifElseStatement.getText()).isEqualTo("IF ELSE Statement");
        assertThat(ifElseStatement.getExpressionStatements()).extracting(ExpressionStatement::getText)
                .containsExactly("System.out.print(1)", "System.out.print(2)");
    }

    @Test
    void shouldVisitElse() {
        String input = "if(false) System.out.print(1); else { System.out.print(2); }";
        JavaParser.IfElseStatementContext c = HELPER.shouldParseToEof(input, JavaParser::ifElseStatement);

        IfElseStatement ifElseStatement = (IfElseStatement) visitor.visit(c, context);

        assertThat(ifElseStatement.getText()).isEqualTo("IF ELSE Statement");
        assertThat(ifElseStatement.getExpressionStatements()).extracting(ExpressionStatement::getText)
                .containsExactly("System.out.print(2)");
    }
}