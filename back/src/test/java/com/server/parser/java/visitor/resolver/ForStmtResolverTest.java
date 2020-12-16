package com.server.parser.java.visitor.resolver;

import com.google.common.collect.Iterables;
import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.value.ObjectWrapperValue;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.context.MethodContext;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ForStmtResolverTest {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private JavaContext javaContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldIterateWhenLackOfCondition() {
        JavaParser.ForStatementContext forContext = mock(JavaParser.ForStatementContext.class);

        assertThat(ForStmtResolver.shouldIterate(javaContext, forContext)).isTrue();
    }

    @Test
    void shouldThrowWhenNotLogic() {
        JavaParser.ExpressionContext expressionContext = mock(JavaParser.ExpressionContext.class);
        Expression condition = mock(Expression.class);
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new StringConstant("str")));
        when(condition.getValue()).thenReturn(value);
        when(javaContext.getVisitor(Expression.class).visit(expressionContext, javaContext)).thenReturn(condition);

        assertThatThrownBy(() -> ForStmtResolver.resolveCondition(javaContext, expressionContext))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: \"str\" nie jest typu logicznego");
    }

    @Test
    void shouldValidateInSeparateContext() {
        ClassContext context = new ClassContext();
        MethodContext methodContext = context.createEmptyMethodContext();
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new StringConstant("init")));
        methodContext.addVariable(new Variable("String", "str", value));

        JavaParser.ForStatementContext c = HELPER.shouldParseToEof("for(int i=0; i<1; i=i+1) str = \"true\";",
                JavaParser::forStatement);

        ForStmtResolver.validateContent(methodContext, c.statement());

        assertThat(methodContext.getVariable("str").getValue()).isSameAs(value);
    }

    @Test
    void shouldThrowWhenSingleVariableDefAsContent() {
        ClassContext context = new ClassContext();
        MethodContext methodContext = context.createEmptyMethodContext();
        JavaParser.ForStatementContext c = HELPER.shouldParseToEof("for(;;) String str = \"true\";",
                JavaParser::forStatement);

        assertThatThrownBy(() -> ForStmtResolver.validateContent(methodContext, c.statement()))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Deklaracja String str = \"true\" nie jest w tym miejscu dozwolona");
    }

    @Test
    void shouldBreakIn() {
        ClassContext context = new ClassContext();
        MethodContext methodContext = context.createEmptyMethodContext();
        methodContext.save(new MethodHeader(Collections.emptyList(), "", "", Collections.emptyList()));
        JavaParser.ForStatementContext c = HELPER.shouldParseToEof("for(;;) { break; fun(); }",
                JavaParser::forStatement);

        List<Statement> statements = ForStmtResolver.resolveContent(methodContext, c, methodContext.getVisitor(Statement.class));
        Statement statement = Iterables.getOnlyElement(statements);
        assertThat(statement.getExpressionStatements()).isEmpty();
    }

    @Test
    void shouldThrowWhenInfinityLoop() {
        ClassContext context = new ClassContext();
        MethodContext methodContext = context.createEmptyMethodContext();
        methodContext.save(new MethodHeader(Collections.emptyList(), "", "", Collections.emptyList()));
        JavaParser.ForStatementContext c = HELPER.shouldParseToEof("for(int i=0; i<1001; i=i+1);",
                JavaParser::forStatement);

        assertThatThrownBy(() -> ForStmtResolver.resolve(methodContext, c))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Ogranicz liczbę iteracji! Maksymalna dostępna liczba " +
                        "iteracji w pętli to 1000");
    }
}