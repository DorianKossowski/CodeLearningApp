package com.server.parser.java.task.verifier;

import com.server.parser.java.ast.TaskAst;
import com.server.parser.java.task.model.ClassModel;
import org.junit.jupiter.api.Test;

class ClassVerifierTest extends VerifierTestBase {

    @Test
    void shouldVerifyClassName() {
        String name = "NAME";
        TaskAst taskAst = new TaskAst(mockClass(name));
        ClassVerifier classVerifier = new ClassVerifier(taskAst);

        classVerifier.verify(ClassModel.builder().withName(name).build());
    }
}