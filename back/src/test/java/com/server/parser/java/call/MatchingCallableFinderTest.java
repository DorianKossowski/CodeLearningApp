package com.server.parser.java.call;

import com.google.common.collect.ImmutableMap;
import com.server.parser.java.ast.ConstructorHeader;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.call.reference.CallReference;
import com.server.parser.java.call.reference.ConstructorCallReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MatchingCallableFinderTest {
    private static final String NAME = "NAME";

    @Mock
    private Method method;
    @Mock
    private MethodHeader methodHeader;

    private MatchingCallableFinder finder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(method.getHeader()).thenReturn(methodHeader);
    }

    @Test
    void shouldReturnNoArgCallable() {
        finder = new MatchingCallableFinder(Collections.singletonMap(methodHeader, method));
        mockHeader(methodHeader);

        Optional<Method> callable = finder.find(new CallReference(NAME), Collections.emptyList());

        assertThat(callable.get()).isSameAs(method);
    }

    private static void mockHeader(MethodHeader methodHeader) {
        when(methodHeader.getName()).thenReturn(NAME);
        when(methodHeader.getArguments()).thenReturn(Collections.emptyList());
    }

    @Test
    void shouldThrowWhenNotMatchingCallable() {
        finder = new MatchingCallableFinder(Collections.emptyMap());

        Optional<Method> callable = finder.find(new CallReference(NAME), Collections.emptyList());

        assertThat(callable).isEmpty();
    }

    @Test
    void shouldNotMatchArgumentsWhenDifferentAmounts() {
        finder = spy(new MatchingCallableFinder(mock(Map.class)));
        VariableDef variableDef = mock(VariableDef.class);
        Expression expression = mock(Expression.class);

        boolean hasMatchingArguments = finder.hasMatchingArguments(Arrays.asList(expression, expression),
                Collections.singletonList(variableDef));

        assertThat(hasMatchingArguments).isFalse();
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void shouldMatchArguments(boolean shouldMatch) {
        finder = spy(new MatchingCallableFinder(mock(Map.class)));
        VariableDef variableDef = mock(VariableDef.class);
        Expression expression = mock(Expression.class);
        doReturn(shouldMatch).when(finder).checkSingleArgumentTypesCorrectness(variableDef, expression);

        boolean hasMatchingArguments = finder.hasMatchingArguments(Collections.singletonList(expression),
                Collections.singletonList(variableDef));

        assertThat(hasMatchingArguments).isEqualTo(shouldMatch);
    }

    static Stream<Arguments> isLabelFulfilledProvider() {
        MethodHeader methodHeader = mock(MethodHeader.class);
        ConstructorHeader constructorHeader = mock(ConstructorHeader.class);
        mockHeader(methodHeader);
        mockHeader(constructorHeader);
        Method method = mock(Method.class);
        Method constructor = mock(Method.class);
        Map<MethodHeader, Method> map = ImmutableMap.<MethodHeader, Method>builder()
                .put(methodHeader, method)
                .put(constructorHeader, constructor)
                .build();
        return Stream.of(
                Arguments.of(map, new CallReference(NAME), method),
                Arguments.of(map, new ConstructorCallReference(NAME), constructor)
        );
    }

    @ParameterizedTest
    @MethodSource("isLabelFulfilledProvider")
    void shouldGetCorrectCallable(Map<MethodHeader, Method> map, CallReference reference, Method expectedCallable) {
        finder = new MatchingCallableFinder(map);

        Optional<Method> callable = finder.find(reference, Collections.emptyList());

        assertThat(callable).contains(expectedCallable);
    }
}