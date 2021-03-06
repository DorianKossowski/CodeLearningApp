package com.server.parser.java.visitor.resolver;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.UninitializedExpression;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.call.CallResolver;
import com.server.parser.java.call.reference.CallReference;
import com.server.parser.java.call.reference.ConstructorCallReference;
import com.server.parser.java.call.reference.PrintCallReference;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.*;
import com.server.parser.java.variable.Variable;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ObjectRefValueResolverTest {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private JavaContext context;
    @Mock
    private Variable variable;
    @Mock
    private ObjectValue value;
    @Mock
    private CallResolver callResolver;

    private ObjectRefValueResolver resolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        resolver = new ObjectRefValueResolver(context);
        when(variable.getValue()).thenReturn(value);
        when(context.getParameters().getMethodName()).thenReturn("");
        when(context.getParameters().getCallResolver()).thenReturn(callResolver);
    }

    static Stream<Arguments> valueToCallOnProvider() {
        return Stream.of(
                Arguments.of(new PrimitiveValue(mock(Literal.class)), "Nie można wywołać metody na prymitywie"),
                Arguments.of(NullValue.INSTANCE, "NullPointerException"),
                Arguments.of(VoidValue.INSTANCE, "Niedozowolone wyrażenie typu void"),
                Arguments.of(new UninitializedValue(mock(UninitializedExpression.class)), "Niezainicjalizowana zmienna null")
        );
    }

    @ParameterizedTest
    @MethodSource("valueToCallOnProvider")
    void shouldThrowOnValueToCallOn(Value value, String expectedExceptionMessageContent) {
        assertThatThrownBy(() -> resolver.validateValueToCallOn(value))
                .isInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: " + expectedExceptionMessageContent);
    }

    @Test
    void shouldResolveSimpleObjectRefExpression() {
        when(context.getVariable("x")).thenReturn(variable);
        String input = "x";
        JavaParser.ObjectRefNameContext c = HELPER.shouldParseToEof(input, JavaParser::objectRefName);

        Value resolvedValue = resolver.resolveValue(c);

        assertThat(resolvedValue).isSameAs(value);
    }

    @Test
    void shouldResolveThis() {
        ObjectValue thisValue = mock(ObjectValue.class);
        when(context.getThisValue()).thenReturn(thisValue);
        String input = "this";
        JavaParser.ObjectRefNameContext c = HELPER.shouldParseToEof(input, JavaParser::objectRefName);

        Value resolvedValue = resolver.resolveValue(c);

        assertThat(resolvedValue).isSameAs(thisValue);
    }


    @Test
    void shouldThrowWhenThisInStaticContext() {
        when(context.getThisValue()).thenReturn(null);
        String input = "this";
        JavaParser.ObjectRefNameContext c = HELPER.shouldParseToEof(input, JavaParser::objectRefName);

        assertThatThrownBy(() -> resolver.resolveValue(c))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Nie można użyć słowa kluczowego this ze statycznego kontekstu");
    }

    @Test
    void shouldResolveCallResult() {
        String input = "fun()";
        JavaParser.ObjectRefNameContext c = HELPER.shouldParseToEof(input, JavaParser::objectRefName);
        CallStatement callStatement = mock(CallStatement.class, RETURNS_DEEP_STUBS);
        when(callStatement.getResult().getValue()).thenReturn(value);
        when(callResolver.resolve(eq(false), any())).thenReturn(callStatement);

        Value resolvedValue = resolver.resolveValue(c);

        assertThat(resolvedValue).isSameAs(value);
    }

    @Test
    void shouldResolveReferenceToObjectRefExpression() {
        when(context.getVariable("x")).thenReturn(variable);
        Value referencedValue = mock(Value.class);
        when(value.getAttribute("y")).thenReturn(referencedValue);
        String input = "x.y";
        JavaParser.ObjectRefNameContext c = HELPER.shouldParseToEof(input, JavaParser::objectRefName);

        Value resolvedValue = resolver.resolveValue(c);

        assertThat(resolvedValue).isSameAs(referencedValue);
    }

    @Test
    void shouldResolveCallOnObjectRefExpression() {
        ObjectRefValueResolver spyResolver = spy(resolver);
        when(context.getVariable("x")).thenReturn(variable);
        String input = "x.fun()";
        JavaParser.ObjectRefNameContext c = HELPER.shouldParseToEof(input, JavaParser::objectRefName);
        CallStatement callStatement = mock(CallStatement.class, RETURNS_DEEP_STUBS);
        Value callValue = mock(Value.class);
        when(callStatement.getResult().getValue()).thenReturn(callValue);
        when(callResolver.resolve(eq(false), any())).thenReturn(callStatement);

        Value resolvedValue = spyResolver.resolveValue(c);

        assertThat(resolvedValue).isSameAs(callValue);
        verify(spyResolver).resolveCallReference(eq(value), any());
    }

    @Test
    void shouldResolvePrintCallReference() {
        String input = "System.out.println";
        JavaParser.CallNameContext c = HELPER.shouldParseToEof(input, JavaParser::callName);

        CallReference reference = resolver.resolveCallReference(null, c);

        assertThat(reference).isExactlyInstanceOf(PrintCallReference.class);
        assertThat(reference.getCallName()).isEqualTo("System.out.println");
        assertThat(reference.getValue()).isEmpty();
    }

    @Test
    void shouldResolveConstructorCallReference() {
        String input = "new X";
        JavaParser.CallNameContext c = HELPER.shouldParseToEof(input, JavaParser::callName);

        CallReference reference = resolver.resolveCallReference(null, c);

        assertThat(reference).isExactlyInstanceOf(ConstructorCallReference.class);
        assertThat(reference.getCallName()).isEqualTo("X");
        assertThat(reference.getValue()).isEmpty();
    }
}