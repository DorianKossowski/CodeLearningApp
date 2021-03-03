package com.server.parser.java.visitor;

import com.google.common.collect.Iterables;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.*;
import com.server.parser.java.ast.constant.IntConstant;
import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.java.ast.expression.Instance;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.VoidExpression;
import com.server.parser.java.ast.statement.*;
import com.server.parser.java.ast.statement.expression_statement.*;
import com.server.parser.java.ast.value.PrimitiveComputableValue;
import com.server.parser.java.ast.value.PrimitiveValue;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.MethodContext;
import com.server.parser.util.exception.BreakStatementException;
import com.server.parser.util.exception.InvalidReturnedExpressionException;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;

class StatementVisitorTest extends JavaVisitorTestBase {
    private final String METHOD_NAME = "methodName";
    private MethodContext methodContext;

    private final StatementVisitor visitor = new StatementVisitor();

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
        methodContext = createMethodContext(METHOD_NAME, "void");
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
        MethodContext methodContext = createRealMethodContext();
        methodContext.save(new MethodHeader(Collections.emptyList(), "", "methodName", Collections.emptyList()),
                mock(JavaParser.MethodBodyContext.class));

        ForStatement statement = (ForStatement) visitor.visit(c, methodContext);

        assertThat(Iterables.getOnlyElement(statement.getExpressionStatements())).isSameAs(BreakExprStatement.INSTANCE);
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

    private Variable createStringVariable(String name) {
        StringConstant stringConstant = new StringConstant("value");
        PrimitiveValue value = new PrimitiveValue(new Literal(stringConstant));
        return new MethodVar("String", name, value);
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

    @Test
    void shouldVisitAttributeAssignment() {
        Value initValue = new PrimitiveValue(new Literal(new IntConstant()));
        FieldVar fieldVar = new FieldVar("int", "b", mock(FieldVarInitExpressionFunction.class), initValue);
        Instance instance = new Instance("MyType", Collections.singletonMap("b", fieldVar));
        MethodVarDef varDef = new MethodVarDef("", "MyType", "a", instance, false);
        methodContext.addVariable(new MethodVar(varDef));
        String input = "a.b = 1";
        JavaParser.AssignmentContext c = HELPER.shouldParseToEof(input, JavaParser::assignment);

        Assignment assignment = (Assignment) visitor.visit(c, methodContext);

        assertThat(assignment.getText()).isEqualTo(input);
        assertThat(assignment.getId()).isEqualTo("a.b");
        assertThat(assignment.getValue().getText()).isEqualTo("1");
        Variable updatedVar = methodContext.getVariable("a");
        assertThat((updatedVar.getValue()).getAttribute("b").getExpression().getResolvedText()).isEqualTo("1");
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
        context.setName("");
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
        realMethodContext.addVariable(new MethodVar("int", "i", new PrimitiveComputableValue(new Literal(new IntConstant(0)))));
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
        realMethodContext.addVariable(new MethodVar("int", "i", new PrimitiveComputableValue(new Literal(new IntConstant(0)))));
        DoWhileStatement doWhileStatement = (DoWhileStatement) visitor.visit(c, realMethodContext);

        assertThat(doWhileStatement.getText()).isEqualTo("DO WHILE Statement");
        assertThat(doWhileStatement.getExpressionStatements()).extracting(ExpressionStatement::getResolved,
                statement -> statement.getProperty(StatementProperties.DO_WHILE_ITERATION))
                .containsExactly(tuple("System.out.print(1)", "0"), tuple("i=1", "0"),
                        tuple("System.out.print(2)", "1"), tuple("i=2", "1"));
    }

    //*** RETURN ***//
    @Test
    void shouldVisitVoidReturn() {
        JavaParser.ReturnStatementContext c = HELPER.shouldParseToEof("return", JavaParser::returnStatement);

        ReturnExprStatement statement = (ReturnExprStatement) visitor.visit(c, methodContext);

        assertThat(statement.getExpression()).isSameAs(VoidExpression.INSTANCE);
        assertThat(statement.getResolved()).isEqualTo("return");
    }

    @Test
    void shouldVisitReturn() {
        JavaParser.ReturnStatementContext c = HELPER.shouldParseToEof("return 1+1", JavaParser::returnStatement);

        ReturnExprStatement statement = (ReturnExprStatement) visitor.visit(c, createMethodContext(METHOD_NAME, "int"));

        assertThat(statement.getExpression()).isExactlyInstanceOf(Literal.class);
        assertThat(statement.getResolved()).isEqualTo("return 2");
    }

    @Test
    void shouldThrowWhenInvalidReturnedExpression() {
        JavaParser.ReturnStatementContext c = HELPER.shouldParseToEof("return 1", JavaParser::returnStatement);

        assertThatThrownBy(() -> visitor.visit(c, methodContext))
                .isExactlyInstanceOf(InvalidReturnedExpressionException.class)
                .hasMessage("Problem podczas rozwiązywania: Zwracany element 1 nie jest typu void");
    }
}