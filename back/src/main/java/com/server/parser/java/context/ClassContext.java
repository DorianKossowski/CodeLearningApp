package com.server.parser.java.context;

import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;

public class ClassContext implements JavaContext {
    private final Map<String, Variable> nameToField = new HashMap<>();
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
    public void addField(Variable var) {
        String varName = var.getName();
        nameToField.computeIfPresent(varName, (key, $) -> {
            throw new ResolvingException("Pole " + key + " już istnieje");
        });
        nameToField.put(varName, var);
    }

    public Variable getField(String name) {
        return nameToField.get(name);
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

    @Override
    public JavaContext createLocalContext() {
        throw new UnsupportedOperationException();
    }
}