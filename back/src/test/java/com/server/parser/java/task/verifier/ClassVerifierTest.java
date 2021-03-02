package com.server.parser.java.task.verifier;

import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.Task;
import com.server.parser.java.task.model.ClassModel;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

class ClassVerifierTest extends VerifierTestBase {

    @Test
    void shouldVerifyClassModifiers() {
        String modifier = "MODIFIER";
        ClassAst classAst = mock(ClassAst.class, RETURNS_DEEP_STUBS);
        when(classAst.getHeader().getModifiers()).thenReturn(Collections.singletonList(modifier));

        Task task = new Task(classAst, Collections.emptyList(), Collections.emptyList());
        ClassVerifier classVerifier = new ClassVerifier(task);

        classVerifier.verify(ClassModel.builder().withModifiers(Collections.singletonList(modifier)).build());
    }

    @Test
    void shouldVerifyClassName() {
        String name = "NAME";
        Task task = new Task(mockClass(name), Collections.emptyList(), Collections.emptyList());
        ClassVerifier classVerifier = new ClassVerifier(task);

        classVerifier.verify(ClassModel.builder().withName(name).build());
    }
}