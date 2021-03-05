package com.server.parser.java.context;

import com.google.common.collect.ImmutableMap;
import com.server.parser.java.variable.Variable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalContext extends DelegatingContext {
    private final Map<String, Variable> localNameToVariable = new HashMap<>();

    LocalContext(JavaContext context) {
        super(context);
        setParameters(context.getParameters());
    }

    @Override
    public JavaContext createLocalContext() {
        return new LocalContext(this);
    }

    @Override
    Map<String, Variable> getImmutableVariables() {
        return ImmutableMap.copyOf(getConcatenatedNameToVariables());
    }

    private Map<String, Variable> getConcatenatedNameToVariables() {
        return Stream.concat(super.getImmutableVariables().entrySet().stream(), localNameToVariable.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void addVariable(Variable var) {
        localNameToVariable.put(getValidatedVariableName(var), var);
    }

    @Override
    public Variable getVariable(String var) {
        if (localNameToVariable.containsKey(var)) {
            return localNameToVariable.get(var);
        }
        return super.getVariable(var);
    }
}