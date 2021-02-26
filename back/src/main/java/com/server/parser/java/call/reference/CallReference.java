package com.server.parser.java.call.reference;

import com.server.parser.java.ast.Variable;

import java.util.Objects;
import java.util.Optional;

public class CallReference {
    private final Variable variable;
    private final String callName;

    public CallReference(String callName) {
        this(null, callName);
    }

    public CallReference(Variable variable, String callName) {
        this.variable = variable;
        this.callName = Objects.requireNonNull(callName, "callName cannot be null");
    }

    public Optional<Variable> getVariable() {
        return Optional.ofNullable(variable);
    }

    public String getCallName() {
        return callName;
    }
}