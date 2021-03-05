package com.server.parser.java.context;

import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.variable.FieldVar;
import com.server.parser.util.exception.ResolvingException;

import java.util.HashMap;
import java.util.Map;

public class ClassContext extends JavaContext {
    private final Map<String, FieldVar> nameToField = new HashMap<>();

    @Override
    public ObjectValue getThisValue() {
        return null;
    }

    public MethodContext createEmptyMethodContext() {
        return new MethodContext(this);
    }

    public void addField(FieldVar fieldVar) {
        String varName = fieldVar.getName();
        nameToField.computeIfPresent(varName, (key, $) -> {
            throw new ResolvingException("Pole " + key + " już istnieje");
        });
        nameToField.put(varName, fieldVar);
    }

    @Override
    public Map<String, FieldVar> getFields() {
        return nameToField;
    }
}