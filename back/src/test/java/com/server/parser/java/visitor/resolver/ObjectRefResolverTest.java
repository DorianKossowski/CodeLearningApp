package com.server.parser.java.visitor.resolver;

import com.server.parser.ParserTestHelper;
import com.server.parser.java.JavaLexer;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.expression.UninitializedExpression;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.value.*;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

class ObjectRefResolverTest {
    private static final ParserTestHelper<JavaParser> HELPER = new ParserTestHelper<>(JavaLexer::new, JavaParser::new);

    @Mock
    private JavaContext context;

    private ObjectRefResolver resolver;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        resolver = new ObjectRefResolver(context);
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
}