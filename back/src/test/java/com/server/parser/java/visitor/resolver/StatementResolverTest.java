package com.server.parser.java.visitor.resolver;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.constant.StringConstant;
import com.server.parser.java.context.ClassContext;
import com.server.parser.java.context.ContextParameters;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.context.MethodContext;
import com.server.parser.java.value.ObjectWrapperValue;
import com.server.parser.java.variable.MethodVar;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class StatementResolverTest {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private JavaContext javaContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldThrowWhenNotLogic() {
        JavaParser.ExpressionContext expressionContext = mock(JavaParser.ExpressionContext.class);
        Expression condition = mock(Expression.class);
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new StringConstant("str")));
        when(condition.getValue()).thenReturn(value);
        when(javaContext.resolveExpression(expressionContext)).thenReturn(condition);

        assertThatThrownBy(() -> ForStmtResolver.resolveCondition(javaContext, expressionContext))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: \"str\" nie jest typu logicznego");
    }

    @Test
    void shouldValidateInSeparateContext() {
        ClassContext context = new ClassContext();
        MethodContext methodContext = context.createEmptyMethodContext();
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new StringConstant("init")));
        methodContext.addVariable(new MethodVar("String", "str", value));

        JavaParser.StatementContext c = HELPER.shouldParseToEof("str = \"true\";", JavaParser::statement);

        ForStmtResolver.validateLoopContent(methodContext, c);

        assertThat(methodContext.getVariable("str").getValue()).isSameAs(value);
    }

    @Test
    void shouldThrowWhenSingleVariableDefAsContent() {
        ClassContext context = new ClassContext();
        context.setParameters(ContextParameters.createClassContextParameters(""));
        MethodContext methodContext = context.createEmptyMethodContext();
        MethodHeader methodHeader = mock(MethodHeader.class, RETURNS_DEEP_STUBS);
        when(methodHeader.getName()).thenReturn("");
        methodContext.save(methodHeader, mock(JavaParser.MethodBodyContext.class));
        JavaParser.StatementContext c = HELPER.shouldParseToEof("String str = \"true\";", JavaParser::statement);

        assertThatThrownBy(() -> ForStmtResolver.validateLoopContent(methodContext, c))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Deklaracja String str = \"true\" nie jest w tym miejscu dozwolona");
    }
}