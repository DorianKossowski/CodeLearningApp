package com.server.parser.java.context;

import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;

public class ClassContext implements JavaContext {
    private final Map<MethodHeader, MethodContext> methodWithContext = new HashMap<>();
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public MethodContext createEmptyMethodContext() {
        return new MethodContext(this);
    }

    void saveCurrentMethodContext(MethodContext methodContext, MethodHeader methodHeader) {
        if (methodWithContext.containsKey(methodHeader)) {
            throw new ResolvingException(String.format("Metoda %s już istnieje", methodHeader));
        }
        methodWithContext.put(methodHeader, methodContext);
    }

    Map<MethodHeader, MethodContext> getMethodWithContext() {
        return methodWithContext;
    }

    @Override
    public Variable getVariable(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addVariable(Variable variable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateVariable(String name, Expression expression) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getMethodName() {
        throw new UnsupportedOperationException();
    }
}