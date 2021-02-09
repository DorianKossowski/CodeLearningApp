package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.constant.IntConstant;
import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.statement.*;
import com.server.parser.java.ast.value.*;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.MethodContext;
import com.server.parser.util.exception.BreakStatementException;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class StatementVisitorTest extends JavaVisitorTestBase {
    private final String METHOD_NAME = "methodName";
    private final StatementVisitor visitor = new StatementVisitor();

    private MethodContext methodContext;

    @BeforeEach
    void setUp() {
        methodContext = createMethodContext(METHOD_NAME);
    }

    @Test
    void shouldVisitBlockStatement() {
        String input = "{ int a = 1; boolean b = false; }";
        JavaParser.BlockStatementContext c = HELPER.shouldParseToEof(input, JavaParser::blockStatement);

        BlockStatement statement = (BlockStatement) visitor.visit(c, methodContext);

        assertThat(statement.getText()).isEqualTo("{ BLOCK STATEMENT }");
        assertThat(statement.getExpressionStatements()).extracting(Statement::getText)
                .containsExactly("int a = 1", "boolean b = false");
    }

    @Test
    void shouldVisitBlockStatementWithBreak() {
        String input = "for(int i=0; i<1; i=i+1){  { break; }  boolean b = false; }";
        JavaParser.ForStatementContext c = HELPER.shouldParseToEof(input, JavaParser::forStatement);
        MethodContext methodContext = new ClassContext().createEmptyMethodContext();
        methodContext.save(new MethodHeader(Collections.emptyList(), "", "", Collections.emptyList()), mock(JavaParser.MethodBodyContext.class));

        ForStatement statement = (ForStatement) visitor.visit(c, methodContext);

        assertThat(statement.getExpressionStatements()).isEmpty();
    }

    //*** METHOD CALL ***//
    @Test
    void shouldVisitMethodCall() {
        String input = "System.out.print(\"Hello World\")";
        JavaParser.CallContext c = HELPER.shouldParseToEof(input, JavaParser::call);

        Call call = (Call) visitor.visit(c, methodContext);

        assertThat(call.getText()).isEqualTo(input);
        assertThat(call.printMethodName()).isEqualTo(METHOD_NAME);
        assertThat(call.getName()).isEqualTo("System.out.print");
        assertThat(Iterables.getOnlyElement(call.getArgs()).getText()).isEqualTo("\"Hello World\"");
    }

    @Disabled
    @Test
    void shouldVisitMethodCallWithoutArgs() {
        MethodHeader header = new MethodHeader(Collections.emptyList(), "", "someMethod", Collections.emptyList());
        context.getCallHandler().getCallableKeeper().keepCallable(new Method(methodContext, header, mock(JavaParser.MethodBodyContext.class)));
        String input = "someMethod()";
        JavaParser.CallContext c = HELPER.shouldParseToEof(input, JavaParser::call);

        Call call = (Call) visitor.visit(c, methodContext);

        assertThat(call.getText()).isEqualTo(input);
        assertThat(call.printMethodName()).isEqualTo(METHOD_NAME);
        assertThat(call.getName()).isEqualTo("someMethod");
        assertThat(call.getArgs()).isEmpty();
    }

    @Disabled
    @Test
    void shouldGetCorrectMethodCallValue() {
        MethodHeader header = new MethodHeader(Collections.emptyList(), "", "someMethod", Collections.emptyList());
        context.getCallHandler().getCallableKeeper().keepCallable(new Method(methodContext, header, mock(JavaParser.MethodBodyContext.class)));
        methodContext.addVariable(createStringVariable("var"));
        String input = "someMethod(\"literal\", var)";
        JavaParser.CallContext c = HELPER.shouldParseToEof(input, JavaParser::call);

        Call call = (Call) visitor.visit(c, methodContext);

        assertThat(call.getResolved()).isEqualTo("someMethod(\"literal\", \"value\")");
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
        String input = "final String a = \"str\"";
        JavaParser.MethodVarDecContext c = HELPER.shouldParseToEof(input, JavaParser::methodVarDec);

        VariableDef variableDef = (VariableDef) visitor.visit(c, methodContext);

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

        VariableDef variableDef = (VariableDef) visitor.visit(c, context);

        assertThat(variableDef.getText()).isEqualTo("String a = \"str\"");
        assertThat(variableDef.getResolved()).isEqualTo(input);
        assertVariableDec(variableDef, Arrays.asList("private", "static"), "String", "a");
        assertThat(variableDef.getValue()).extracting(Value::toString)
                .isEqualTo("\"str\"");
    }

    //*** ASSIGNMENT ***//
    @Test
    void shouldVisitAssignment() {
        methodContext.addVariable(createStringVariable("a"));
        String input = "a = \"str\"";
        JavaParser.AssignmentContext c = HELPER.shouldParseToEof(input, JavaParser::assignment);

        Assignment assignment = (Assignment) visitor.visit(c, methodContext);

        assertThat(assignment.getText()).isEqualTo(input);
        assertThat(assignment.getId()).isEqualTo("a");
        assertThat(assignment.getValue().getText()).isEqualTo("\"str\"");
    }

    @Test
    void shouldThrowWhenInvalidAssignment() {
        methodContext.addVariable(createStringVariable("a"));
        String input = "a = 5";
        JavaParser.AssignmentContext c = HELPER.shouldParseToEof(input, JavaParser::assignment);

        assertThatThrownBy(() -> visitor.visit(c, methodContext))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Wyrażenie 5 nie jest typu String");
    }

    //*** IF ***//
    @Test
    void shouldVisitIf() {
        String input = "if(true) { System.out.print(1); if(true) { System.out.print(2); } }";
        JavaParser.IfElseStatementContext c = HELPER.shouldParseToEof(input, JavaParser::ifElseStatement);

        IfElseStatement ifElseStatement = (IfElseStatement) visitor.visit(c, createRealMethodContext());

        assertThat(ifElseStatement.getText()).isEqualTo("IF ELSE Statement");
        assertThat(ifElseStatement.getExpressionStatements()).extracting(ExpressionStatement::getText)
                .containsExactly("System.out.print(1)", "System.out.print(2)");
    }

    private MethodContext createRealMethodContext() {
        ClassContext context = new ClassContext();
        MethodContext methodContext = context.createEmptyMethodContext();
        MethodHeader methodHeader = new MethodHeader(Collections.emptyList(), "", "", Collections.emptyList());
        methodContext.save(methodHeader, mock(JavaParser.MethodBodyContext.class));
        return methodContext;
    }

    @Test
    void shouldVisitElse() {
        String input = "if(false) System.out.print(1); else { System.out.print(2); }";
        JavaParser.IfElseStatementContext c = HELPER.shouldParseToEof(input, JavaParser::ifElseStatement);

        IfElseStatement ifElseStatement = (IfElseStatement) visitor.visit(c, createRealMethodContext());

        assertThat(ifElseStatement.getText()).isEqualTo("IF ELSE Statement");
        assertThat(ifElseStatement.getExpressionStatements()).extracting(ExpressionStatement::getText)
                .containsExactly("System.out.print(2)");
    }

    //*** BREAK ***//
    @Test
    void shouldVisitBreak() {
        JavaParser.BreakStatementContext c = HELPER.shouldParseToEof("break", JavaParser::breakStatement);

        assertThatThrownBy(() -> visitor.visit(c, context))
                .isExactlyInstanceOf(BreakStatementException.class)
                .hasMessage("Problem podczas rozwiązywania: 'break' poza instrukcją switch oraz pętlą");
    }

    //*** EMPTY ***//
    @Test
    void shouldVisitEmpty() {
        JavaParser.EmptyStatementContext c = HELPER.shouldParseToEof(";", JavaParser::emptyStatement);

        assertThat(visitor.visit(c, context)).isSameAs(EmptyStatement.INSTANCE);
    }

    //*** FOR ***//
    @Test
    void shouldVisitFor() {
        String input = "for(int i=0; i<2; i=i+1) { System.out.print(i+1); }";
        JavaParser.ForStatementContext c = HELPER.shouldParseToEof(input, JavaParser::forStatement);

        ForStatement forStatement = (ForStatement) visitor.visit(c, createRealMethodContext());

        assertThat(forStatement.getText()).isEqualTo("FOR Statement");
        assertThat(forStatement.getExpressionStatements()).extracting(ExpressionStatement::getResolved,
                statement -> statement.getProperty(StatementProperties.FOR_ITERATION))
                .containsExactly(tuple("System.out.print(1)", "0"), tuple("System.out.print(2)", "1"));
    }

    //*** WHILE ***//
    @Test
    void shouldVisitWhile() {
        String input = "while(i<2) { System.out.print(i+1); i = i+1; }";
        JavaParser.WhileStatementContext c = HELPER.shouldParseToEof(input, JavaParser::whileStatement);

        MethodContext realMethodContext = createRealMethodContext();
        realMethodContext.addVariable(new Variable("int", "i", new PrimitiveComputableValue(new Literal(new IntConstant(0)))));
        WhileStatement whileStatement = (WhileStatement) visitor.visit(c, realMethodContext);

        assertThat(whileStatement.getText()).isEqualTo("WHILE Statement");
        assertThat(whileStatement.getExpressionStatements()).extracting(ExpressionStatement::getResolved,
                statement -> statement.getProperty(StatementProperties.WHILE_ITERATION))
                .containsExactly(tuple("System.out.print(1)", "0"), tuple("i=1", "0"),
                        tuple("System.out.print(2)", "1"), tuple("i=2", "1"));
    }

    //*** DO WHILE ***//
    @Test
    void shouldVisitDoWhile() {
        String input = "do { System.out.print(i+1); i = i+1; } while(i<2);";
        JavaParser.DoWhileStatementContext c = HELPER.shouldParseToEof(input, JavaParser::doWhileStatement);

        MethodContext realMethodContext = createRealMethodContext();
        realMethodContext.addVariable(new Variable("int", "i", new PrimitiveComputableValue(new Literal(new IntConstant(0)))));
        DoWhileStatement doWhileStatement = (DoWhileStatement) visitor.visit(c, realMethodContext);

        assertThat(doWhileStatement.getText()).isEqualTo("DO WHILE Statement");
        assertThat(doWhileStatement.getExpressionStatements()).extracting(ExpressionStatement::getResolved,
                statement -> statement.getProperty(StatementProperties.DO_WHILE_ITERATION))
                .containsExactly(tuple("System.out.print(1)", "0"), tuple("i=1", "0"),
                        tuple("System.out.print(2)", "1"), tuple("i=2", "1"));
    }
}