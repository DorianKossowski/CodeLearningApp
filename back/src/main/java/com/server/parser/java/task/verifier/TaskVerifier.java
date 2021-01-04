package com.server.parser.java.task.verifier;

import com.server.parser.java.ast.TaskAst;
import com.server.parser.java.task.model.ClassModel;
import com.server.parser.java.task.model.MethodModel;
import com.server.parser.java.task.model.StatementModel;
import com.server.parser.java.task.model.VariableModel;

import java.util.Objects;

public class TaskVerifier {
    private final ClassVerifier classVerifier;
    private final MethodVerifier methodVerifier;
    private final ExpressionStatementVerifier expressionStatementVerifier;
    private final VariableVerifier variableVerifier;

    public TaskVerifier(TaskAst taskAst) {
        this.classVerifier = new ClassVerifier(Objects.requireNonNull(taskAst, "taskAst cannot be null"));
        this.methodVerifier = new MethodVerifier(taskAst);
        this.expressionStatementVerifier = new ExpressionStatementVerifier(taskAst);
        this.variableVerifier = new VariableVerifier(taskAst);
    }

    public void verifyClass(ClassModel classModel) {
        classVerifier.verify(classModel);
    }

    public void verifyMethod(MethodModel methodModel) {
        methodVerifier.verify(methodModel);
    }

    public void verifyStatement(StatementModel statementModel) {
        expressionStatementVerifier.verify(statementModel);
    }

    public void verifyVariable(VariableModel variableModel) {
        variableVerifier.verify(variableModel);
    }
}