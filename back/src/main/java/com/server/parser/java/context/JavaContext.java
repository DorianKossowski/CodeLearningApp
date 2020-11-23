package com.server.parser.java.context;

import com.server.parser.java.JavaVisitorsRegistry;
import com.server.parser.java.ast.AstElement;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.util.exception.ResolvingException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JavaContext implements Serializable {
    private final Map<String, MethodContext> methodWithContext = new HashMap<>();

    private String currentClassName;
    private MethodContext currentMethodContext;

    public MethodContext createCurrentMethodContext(String methodName) {
        if (methodWithContext.containsKey(methodName)) {
            throw new ResolvingException(String.format("Metoda %s już istnieje", methodName));
        }
        MethodContext methodContext = new MethodContext(methodName);
        methodWithContext.put(methodName, methodContext);
        currentMethodContext = methodContext;
        return currentMethodContext;
    }

    public String getCurrentClassName() {
        return currentClassName;
    }

    public void setCurrentClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    public MethodContext getCurrentMethodContext() {
        return currentMethodContext;
    }

    public <T extends AstElement> JavaVisitor<T> getVisitor(Class<T> elementClass) {
        return JavaVisitorsRegistry.get(elementClass);
    }
}