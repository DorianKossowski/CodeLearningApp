package com.server.parser.java.visitor.resolver;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.value.ObjectWrapperValue;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.context.MethodContext;
import com.server.parser.java.visitor.StatementVisitor;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IfStmtResolverTest {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private JavaContext javaContext;

    private IfStmtResolver resolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        resolver = new IfStmtResolver(javaContext, mock(StatementVisitor.StatementVisitorInternal.class));
    }

    @Test
    void shouldThrowWhenNotLogic() {
        JavaParser.ExpressionContext expressionContext = mock(JavaParser.ExpressionContext.class);
        Expression condition = mock(Expression.class);
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new StringConstant("str")));
        when(condition.getValue()).thenReturn(value);
        when(javaContext.getVisitor(Expression.class).visit(expressionContext, javaContext)).thenReturn(condition);

        assertThatThrownBy(() -> resolver.resolveCondition(expressionContext))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: \"str\" nie jest typu logicznego");
    }

    @Test
    void shouldValidateInSeparateContext() {
        JavaContext javaContext = new JavaContext();
        resolver = new IfStmtResolver(javaContext, mock(StatementVisitor.StatementVisitorInternal.class));
        MethodContext methodContext = javaContext.createCurrentMethodContext("");
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new StringConstant("init")));
        methodContext.addVar(new Variable("String", "str", value));

        JavaParser.IfElseStatementContext c = HELPER.shouldParseToEof("if(true) str = \"true\"; else str = \"false\";",
                JavaParser::ifElseStatement);

        resolver.validateBranchesContent(c);

        assertThat(methodContext.getVariable("str").getValue()).isSameAs(value);
    }
}