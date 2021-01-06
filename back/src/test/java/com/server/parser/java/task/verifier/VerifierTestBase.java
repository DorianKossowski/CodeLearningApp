package com.server.parser.java.task.verifier;

import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.TaskAst;
import com.server.parser.java.ast.statement.Statement;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

abstract class VerifierTestBase {

    protected TaskAst mockTask(List<Method> methods) {
        return mockTask(methods, Collections.emptyList());
    }

    protected TaskAst mockTask(List<Method> methods, List<Statement> statements) {
        statements.forEach(statement -> doCallRealMethod().when(statement).getExpressionStatements());
        TaskAst taskAst = mock(TaskAst.class, RETURNS_DEEP_STUBS);
        when(taskAst.getClassAst().getBody().getMethods()).thenReturn(methods);
        when(taskAst.getCalledStatements()).thenReturn(statements);
        return taskAst;
    }

    protected ClassAst mockClass(String name) {
        ClassAst classAst = mock(ClassAst.class, RETURNS_DEEP_STUBS);
        when(classAst.getHeader().getName()).thenReturn(name);
        return classAst;
    }

    protected Method mockMethod(String name) {
        Method method = mock(Method.class, RETURNS_DEEP_STUBS);
        when(method.getHeader().getName()).thenReturn(name);
        return method;
    }
}