package com.server.parser.java.context;

import com.server.parser.java.ast.FieldVar;
import com.server.parser.java.call.CallHandler;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassContext implements JavaContext {
    private final CallHandler callHandler = new CallHandler();
    private final Map<String, FieldVar> nameToField = new HashMap<>();
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

    @Override
    public void addField(FieldVar fieldVar) {
        String varName = fieldVar.getName();
        nameToField.computeIfPresent(varName, (key, $) -> {
            throw new ResolvingException("Pole " + key + " już istnieje");
        });
        nameToField.put(varName, fieldVar);
    }

    Map<String, FieldVar> getFields() {
        return nameToField;
    }

    @Override
    public CallHandler getCallHandler() {
        return callHandler;
    }

    @Override
    public Map<String, FieldVar> getStaticFields() {
        return nameToField.entrySet().stream()
                .filter(entry -> entry.getValue().isStatic())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public void setStaticFields(Map<String, FieldVar> nameToField) {
        this.nameToField.putAll(nameToField);
    }

    @Override
    public String getMethodName() {
        return "";
    }
}