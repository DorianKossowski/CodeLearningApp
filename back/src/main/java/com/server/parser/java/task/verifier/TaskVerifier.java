package com.server.parser.java.task.verifier;

import com.server.parser.java.ast.TaskAst;
import com.server.parser.java.task.model.MethodModel;
import com.server.parser.java.task.model.StatementModel;

import java.util.Objects;

public class TaskVerifier {
    private final MethodVerifier methodVerifier;
    private final StatementVerifier statementVerifier;

    public TaskVerifier(TaskAst taskAst) {
        this.methodVerifier = new MethodVerifier(Objects.requireNonNull(taskAst, "taskAst cannot be null"));
        this.statementVerifier = new StatementVerifier(taskAst);
    }

    public void verifyMethod(MethodModel methodModel) {
        methodVerifier.verify(methodModel);
    }

    public void verifyStatement(StatementModel statementModel) {
        statementVerifier.verify(statementModel);
    }
}