package com.server.parser.java.task.verifier;

import com.server.parser.java.ast.TaskAst;
import com.server.parser.java.task.model.ClassModel;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ClassVerifierTest extends VerifierTestBase {

    @Test
    void shouldVerifyClassName() {
        String name = "NAME";
        TaskAst taskAst = new TaskAst(mockClass(name), Collections.emptyList());
        ClassVerifier classVerifier = new ClassVerifier(taskAst);

        classVerifier.verify(ClassModel.builder().withName(name).build());
    }
}