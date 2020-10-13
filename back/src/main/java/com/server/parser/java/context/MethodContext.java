package com.server.parser.java.context;

import com.google.common.collect.ImmutableMap;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MethodContext {
    private final String methodName;
    private final Map<String, String> varToValue = new HashMap<>();

    MethodContext(String name) {
        this.methodName = Objects.requireNonNull(name, "name cannot be null");
    }

    public String getMethodName() {
        return methodName;
    }

    public Map<String, String> getVarToValue() {
        return ImmutableMap.copyOf(varToValue);
    }

    public void addVar(String var, String value) {
        varToValue.put(var, value);
    }

    public String getValue(String var) {
        return varToValue.computeIfAbsent(var, key -> {
            throw new ResolvingException("Obiekt " + key + " nie istnieje");
        });
    }
}