package com.server.parser.java.visitor.resolver;

import com.server.parser.java.ast.constant.StringConstant;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.Literal;
import com.server.parser.java.ast.value.ObjectWrapperValue;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IfStmtResolverTest {
    private final IfStmtResolver resolver = new IfStmtResolver();

    @Test
    void shouldThrowWhenNotLogic() {
        Expression condition = mock(Expression.class);
        ObjectWrapperValue value = new ObjectWrapperValue(new Literal(new StringConstant("str")));
        when(condition.getValue()).thenReturn(value);

        assertThatThrownBy(() -> resolver.resolveCondition(condition))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: \"str\" nie jest typu logicznego");
    }
}