package com.server.parser.java.context;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.FieldVar;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.Variable;
import com.server.parser.util.exception.ResolvingException;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MethodContextTest {

    @Test
    void shouldThrowWhenKeyAlreadyExists() {
        MethodContext methodContext = new MethodContext(new ClassContext());
        Variable var = mock(Variable.class);
        when(var.getName()).thenReturn("var");
        methodContext.addVariable(var);

        assertThatThrownBy(() -> methodContext.addVariable(var))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt var już istnieje");
    }

    @Test
    void shouldThrowWhenKeyNotExists() {
        MethodContext methodContext = new MethodContext(new ClassContext());

        assertThatThrownBy(() -> methodContext.getVariable("var"))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Obiekt var nie istnieje");
    }

    @Test
    void shouldGetField() {
        ClassContext classContext = new ClassContext();
        FieldVar var = mock(FieldVar.class);
        when(var.getName()).thenReturn("var");
        when(var.isStatic()).thenReturn(false);
        classContext.addField(var);
        MethodContext methodContext = new MethodContext(classContext);
        methodContext.save(new MethodHeader(Collections.emptyList(), "", "", Collections.emptyList()),
                mock(JavaParser.MethodBodyContext.class));

        assertThat(methodContext.getVariable("var")).isSameAs(var);
    }

    @Test
    void shouldThrowWhenGettingNonStaticFieldFromStatic() {
        ClassContext classContext = new ClassContext();
        FieldVar var = mock(FieldVar.class);
        when(var.getName()).thenReturn("var");
        when(var.isStatic()).thenReturn(false);
        classContext.addField(var);
        MethodContext methodContext = new MethodContext(classContext);
        methodContext.save(new MethodHeader(Collections.singletonList("static"), "", "", Collections.emptyList()),
                mock(JavaParser.MethodBodyContext.class));

        assertThatThrownBy(() -> methodContext.getVariable("var"))
                .isExactlyInstanceOf(ResolvingException.class)
                .hasMessage("Problem podczas rozwiązywania: Nie można użyć var ze statycznego kontekstu");
    }

    @Test
    void shouldGetVar() {
        ClassContext classContext = new ClassContext();
        FieldVar varField = mock(FieldVar.class);
        when(varField.getName()).thenReturn("var");
        classContext.addField(varField);

        MethodContext methodContext = new MethodContext(classContext);
        Variable var = mock(Variable.class);
        when(var.getName()).thenReturn("var");
        methodContext.addVariable(var);

        assertThat(methodContext.getVariable("var")).isSameAs(var);
    }
}