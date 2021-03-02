package com.server.parser.java.task.verifier;

import com.google.common.base.VerifyException;
import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.Task;
import com.server.parser.java.ast.statement.expression_statement.FieldVarDef;
import com.server.parser.java.task.model.FieldModel;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class FieldVerifierTest extends VerifierTestBase {

    @Test
    void shouldVerifyFieldName() {
        String name = "NAME";
        FieldVarDef variableDef = mock(FieldVarDef.class);
        when(variableDef.getName()).thenReturn(name);
        FieldVerifier fieldVerifier = new FieldVerifier(mockTaskWithField(variableDef));

        fieldVerifier.verify(FieldModel.builder().withName(name).build());
    }

    private Task mockTaskWithField(FieldVarDef field) {
        Task task = mock(Task.class);
        ClassAst classAst = mock(ClassAst.class, RETURNS_DEEP_STUBS);
        when(classAst.getBody().getFields()).thenReturn(Collections.singletonList(field));
        when(task.getClassAst()).thenReturn(classAst);
        return task;
    }

    @Test
    void shouldThrowDuringVerifyingFieldName() {
        FieldVarDef variableDef = mock(FieldVarDef.class);
        when(variableDef.getName()).thenReturn("");
        FieldVerifier fieldVerifier = new FieldVerifier(mockTaskWithField(variableDef));

        assertThatThrownBy(() -> fieldVerifier.verify(FieldModel.builder().withName("NOT_NAME").build()))
                .isExactlyInstanceOf(VerifyException.class)
                .hasMessageContaining("Oczekiwane pole \"NOT_NAME\" nie istnieje");
    }

    @Test
    void shouldHasSameModifiers() {
        FieldVarDef variableDef = mock(FieldVarDef.class);
        when(variableDef.getModifiers()).thenReturn(Collections.singletonList("public"));
        FieldVerifier fieldVerifier = new FieldVerifier(mockTaskWithField(variableDef));

        fieldVerifier.verify(FieldModel.builder().withModifiers(Collections.singletonList("public")).build());
    }

    @Test
    void shouldHasSameType() {
        FieldVarDef variableDef = mock(FieldVarDef.class);
        when(variableDef.getType()).thenReturn("int");
        FieldVerifier fieldVerifier = new FieldVerifier(mockTaskWithField(variableDef));

        fieldVerifier.verify(FieldModel.builder().withType("int").build());
    }

    @Test
    void shouldHasSameValue() {
        FieldVarDef variableDef = mock(FieldVarDef.class, RETURNS_DEEP_STUBS);
        when(variableDef.getValue().getExpression().getResolvedText()).thenReturn("1");
        FieldVerifier fieldVerifier = new FieldVerifier(mockTaskWithField(variableDef));

        fieldVerifier.verify(FieldModel.builder().withInitText("1").build());
    }
}