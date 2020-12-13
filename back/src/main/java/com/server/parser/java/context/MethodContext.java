package com.server.parser.java.context;

import com.google.common.collect.ImmutableMap;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.value.Value;
import com.server.parser.util.ValuePreparer;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MethodContext implements JavaContext {
    private final ClassContext classContext;
    private MethodHeader methodHeader;
    private final Map<String, Variable> nameToVariable = new HashMap<>();

    MethodContext(ClassContext classContext) {
        this.classContext = Objects.requireNonNull(classContext, "classContext cannot be null");
    }

    public void save(MethodHeader methodHeader) {
        this.methodHeader = Objects.requireNonNull(methodHeader, "methodHeader cannot be null");
        classContext.saveCurrentMethodContext(this, methodHeader);
    }

    public String getClassName() {
        return classContext.getName();
    }

    public Map<String, Variable> getNameToVariable() {
        return ImmutableMap.copyOf(nameToVariable);
    }

    @Override
    public String getMethodName() {
        return methodHeader.getName();
    }

    @Override
    public void addVariable(Variable var) {
        String varName = var.getName();
        nameToVariable.computeIfPresent(varName, (key, $) -> {
            throw new ResolvingException("Obiekt " + key + " już istnieje");
        });
        nameToVariable.put(varName, var);
    }

    @Override
    public void updateVariable(String var, Expression expression) {
        Variable variable = getVariable(var);
        Value newValue = ValuePreparer.prepare(variable.getType(), expression);
        variable.setValue(newValue);
    }

    @Override
    public Variable getVariable(String var) {
        return nameToVariable.computeIfAbsent(var, key -> {
            throw new ResolvingException("Obiekt " + key + " nie istnieje");
        });
    }
}