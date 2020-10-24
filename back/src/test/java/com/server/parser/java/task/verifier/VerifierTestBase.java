package com.server.parser.java.task.verifier;

import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.TaskAst;
import com.server.parser.java.ast.statement.Statement;

import java.util.List;

import static org.mockito.Mockito.*;

abstract class VerifierTestBase {

    protected TaskAst mockTask(List<Method> methods) {
        TaskAst taskAst = mock(TaskAst.class, RETURNS_DEEP_STUBS);
        when(taskAst.getClassAst().getBody().getMethods()).thenReturn(methods);
        return taskAst;
    }

    protected Method mockMethod(String name) {
        Method method = mock(Method.class, RETURNS_DEEP_STUBS);
        when(method.getHeader().getName()).thenReturn(name);
        return method;
    }

    protected Method mockMethod(String name, List<Statement> statements) {
        Method method = mockMethod(name);
        when(method.getBody().getStatements()).thenReturn(statements);
        return method;
    }
}