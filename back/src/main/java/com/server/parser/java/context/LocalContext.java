package com.server.parser.java.context;

import com.google.common.collect.ImmutableMap;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.variable.Variable;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalContext extends DelegatingContext {
    private final Map<String, Variable> localNameToVariable = new HashMap<>();

    LocalContext(JavaContext context) {
        super(context);
    }

    @Override
    Map<String, Variable> getNameToVariable() {
        return ImmutableMap.copyOf(getConcatenatedNameToVariables());
    }

    @Override
    public JavaContext createLocalContext() {
        return new LocalContext(this);
    }

    private Map<String, Variable> getConcatenatedNameToVariables() {
        return Stream.concat(super.getNameToVariable().entrySet().stream(), localNameToVariable.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void addVariable(Variable var) {
        String varName = var.getName();
        validateVariableExistence(varName, getNameToVariable());
//        validateVariableExistence(varName, localNameToVariable);
        localNameToVariable.put(varName, var);
    }

    private void validateVariableExistence(String varName, Map<String, Variable> nameToVariable) {
        if (nameToVariable.containsKey(varName)) {
            throw new ResolvingException("Obiekt " + varName + " już istnieje");
        }
    }

    @Override
    public void updateVariable(String name, Expression expression) {
        super.updateVariable(name, expression);
    }

    @Override
    public Variable getVariable(String var) {
        if (localNameToVariable.containsKey(var)) {
            return localNameToVariable.get(var);
        }
        return super.getVariable(var);
    }
}