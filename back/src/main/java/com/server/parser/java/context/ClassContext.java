package com.server.parser.java.context;

import com.server.parser.java.call.CallResolver;
import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.variable.FieldVar;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassContext implements JavaContext {
    private final CallResolver callResolver = new CallResolver();
    private final Map<String, FieldVar> nameToField = new HashMap<>();
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getClassName() {
        return name;
    }

    @Override
    public boolean isStaticContext() {
        return true;
    }

    @Override
    public ObjectValue getThisValue() {
        return null;
    }

    @Override
    public void setThisValue(ObjectValue thisValue) {
        throw new UnsupportedOperationException();
    }

    public MethodContext createEmptyMethodContext() {
        return new MethodContext(this);
    }

    @Override
    public void addField(FieldVar fieldVar) {
        String varName = fieldVar.getName();
        nameToField.computeIfPresent(varName, (key, $) -> {
            throw new ResolvingException("Pole " + key + " już istnieje");
        });
        nameToField.put(varName, fieldVar);
    }

    Map<String, FieldVar> getFields() {
        return new HashMap<>(nameToField);
    }

    @Override
    public CallResolver getCallResolver() {
        return callResolver;
    }

    @Override
    public Map<String, FieldVar> getStaticFields() {
        return nameToField.entrySet().stream()
                .filter(entry -> entry.getValue().isStatic())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void setFields(Map<String, FieldVar> nameToField) {
        this.nameToField.putAll(nameToField);
    }

    @Override
    public String getMethodName() {
        return "";
    }
}