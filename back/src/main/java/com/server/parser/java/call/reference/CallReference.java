package com.server.parser.java.call.reference;

import com.server.parser.java.value.ObjectValue;

import java.util.Objects;
import java.util.Optional;

public class CallReference {
    private final ObjectValue value;
    private final String callName;

    public CallReference(String callName) {
        this(null, callName);
    }

    public CallReference(ObjectValue value, String callName) {
        this.value = value;
        this.callName = Objects.requireNonNull(callName, "callName cannot be null");
    }

    public Optional<ObjectValue> getValue() {
        return Optional.ofNullable(value);
    }

    public String getCallName() {
        return callName;
    }
}