package com.server.parser.java.call;

import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.CallInvocation;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CallableKeeperTest {
    private static final String NAME = "FUN";

    @Mock
    private Method method;
    @Mock
    private MethodHeader methodHeader;
    @Mock
    private MatchingCallableFinder matchingCallableFinder;

    private CallableKeeper keeper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        keeper = new CallableKeeper(matchingCallableFinder);
        when(method.getHeader()).thenReturn(methodHeader);
    }

    @Test
    void shouldKeepMethodWithContext() {
        keeper.keepCallable(method);

        assertThat(keeper.getCallables()).containsExactly(method);
    }

    @Test
    void shouldThrowWhenMethodAlreadyExists() {
        when(methodHeader.toString()).thenReturn(NAME);
        keeper.keepCallable(method);

        assertThatThrownBy(() -> keeper.keepCallable(method))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Metoda FUN już istnieje");
    }

    static Stream<Arguments> methodHeaderProvider() {
        MethodHeader m1 = new MethodHeader(Collections.emptyList(), "", "M", Collections.emptyList());
        MethodHeader m2 = new MethodHeader(Collections.emptyList(), "", "M",
                Collections.singletonList(mock(VariableDef.class, RETURNS_DEEP_STUBS)));
        MethodHeader m3 = new MethodHeader(Collections.emptyList(), "", "MM", Collections.emptyList());
        MethodHeader c = new MethodHeader(Collections.emptyList(), null, "M", Collections.emptyList());
        return Stream.of(
                Arguments.of(m1, m1, true),
                Arguments.of(m1, m2, false),
                Arguments.of(m2, m2, true),
                Arguments.of(m1, m3, false),
                Arguments.of(m1, c, false),
                Arguments.of(c, c, true)
        );
    }

    @ParameterizedTest
    @MethodSource("methodHeaderProvider")
    void shouldTrySavingCurrentMethodContext(MethodHeader methodHeader1, MethodHeader methodHeader2, boolean shouldThrow) {
        when(method.getHeader()).thenReturn(methodHeader1);
        keeper.keepCallable(method);
        when(method.getHeader()).thenReturn(methodHeader2);
        if (shouldThrow) {
            assertThatThrownBy(() -> keeper.keepCallable(method))
                    .isExactlyInstanceOf(ResolvingException.class);
        } else {
            assertThatCode(() -> keeper.keepCallable(method))
                    .doesNotThrowAnyException();
        }
    }

    @Test
    void shouldGetCallable() {
        String name = "NAME";
        List<Expression> args = Collections.emptyList();
        when(matchingCallableFinder.find(name, args)).thenReturn(Optional.of(method));

        Method callable = keeper.getCallable(new CallInvocation("", "", new CallReference(name), args));

        assertThat(callable).isSameAs(method);
    }

    @Test
    void shouldThrowWhenNoMatchingCallable() {
        when(matchingCallableFinder.find(any(), any())).thenReturn(Optional.empty());
        CallInvocation invocation = mock(CallInvocation.class);
        when(invocation.getText()).thenReturn("TEXT");

        assertThatThrownBy(() -> keeper.getCallable(invocation))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Brak pasującej metody dla wywołania: TEXT");
    }
}