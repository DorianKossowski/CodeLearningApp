package com.server.parser.java.context;

import com.google.common.collect.ImmutableMap;
import com.server.parser.java.ast.Value;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.util.ValuePreparer;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MethodContext {
    private final String methodName;
    private final Map<String, Variable> nameToVariable = new HashMap<>();

    MethodContext(String name) {
        this.methodName = Objects.requireNonNull(name, "name cannot be null");
    }

    public String getMethodName() {
        return methodName;
    }

    public Map<String, Variable> getNameToVariable() {
        return ImmutableMap.copyOf(nameToVariable);
    }

    public void addVar(Variable var) {
        String varName = var.getName();
        nameToVariable.computeIfPresent(varName, (key, $) -> {
            throw new ResolvingException("Obiekt " + key + " już istnieje");
        });
        nameToVariable.put(varName, var);
    }

    public void updateVar(String var, Expression expression) {
        Variable variable = getVariable(var);
        Value newValue = ValuePreparer.prepare(variable.getType(), expression);
        variable.setValue(newValue);
    }

    public Variable getVariable(String var) {
        return nameToVariable.computeIfAbsent(var, key -> {
            throw new ResolvingException("Obiekt " + key + " nie istnieje");
        });
    }
}