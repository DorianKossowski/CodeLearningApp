package com.server.parser.java.context;

import com.server.parser.java.JavaVisitorsRegistry;
import com.server.parser.java.ast.AstElement;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;

public class JavaContext {
    private final Map<String, MethodContext> methodWithContext = new HashMap<>();

    private MethodContext currentMethodContext;

    public void putMethodWithContext(MethodContext methodContext) {
        String methodName = methodContext.getMethodName();
        if (methodWithContext.containsKey(methodName)) {
            throw new ResolvingException(String.format("Metoda %s już istnieje", methodName));
        }
        methodWithContext.put(methodName, methodContext);
        currentMethodContext = methodContext;
    }

    public MethodContext getCurrentMethodContext() {
        return currentMethodContext;
    }

    public <T extends AstElement> JavaVisitor<T> getVisitor(Class<T> elementClass) {
        return JavaVisitorsRegistry.get(elementClass);
    }
}