package com.server.parser.java.task.verifier;

import com.server.parser.java.ast.Task;
import com.server.parser.java.task.model.*;

import java.util.Objects;

public class TaskVerifier {
    private final ClassVerifier classVerifier;
    private final FieldVerifier fieldVerifier;
    private final ConstructorVerifier constructorVerifier;
    private final MethodVerifier methodVerifier;
    private final ExpressionStatementVerifier expressionStatementVerifier;
    private final VariableVerifier variableVerifier;

    public TaskVerifier(Task task) {
        this.classVerifier = new ClassVerifier(Objects.requireNonNull(task, "task cannot be null"));
        this.fieldVerifier = new FieldVerifier(task);
        this.constructorVerifier = new ConstructorVerifier(task);
        this.methodVerifier = new MethodVerifier(task);
        this.expressionStatementVerifier = new ExpressionStatementVerifier(task);
        this.variableVerifier = new VariableVerifier(task);
    }

    public void verifyClass(ClassModel classModel) {
        classVerifier.verify(classModel);
    }

    public void verifyField(FieldModel fieldModel) {
        fieldVerifier.verify(fieldModel);
    }

    public void verifyConstructor(MethodModel methodModel) {
        constructorVerifier.verify(methodModel);
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