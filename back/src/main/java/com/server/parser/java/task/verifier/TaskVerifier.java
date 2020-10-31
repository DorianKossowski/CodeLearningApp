package com.server.parser.java.task.verifier;

import com.server.parser.java.ast.TaskAst;
import com.server.parser.java.task.model.MethodModel;
import com.server.parser.java.task.model.StatementModel;
import com.server.parser.java.task.model.VariableModel;

import java.util.Objects;

public class TaskVerifier {
    private final MethodVerifier methodVerifier;
    private final StatementVerifier statementVerifier;
    private final VariableVerifier variableVerifier;

    public TaskVerifier(TaskAst taskAst) {
        this.methodVerifier = new MethodVerifier(Objects.requireNonNull(taskAst, "taskAst cannot be null"));
        this.statementVerifier = new StatementVerifier(taskAst);
        this.variableVerifier = new VariableVerifier(taskAst);
    }

    public void verifyMethod(MethodModel methodModel) {
        methodVerifier.verify(methodModel);
    }

    public void verifyStatement(StatementModel statementModel) {
        statementVerifier.verify(statementModel);
    }

    public void verifyVariable(VariableModel variableModel) {
        variableVerifier.verify(variableModel);
    }
}