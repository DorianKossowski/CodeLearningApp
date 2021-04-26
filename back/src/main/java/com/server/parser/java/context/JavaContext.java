package com.server.parser.java.context;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.variable.FieldVar;
import com.server.parser.java.variable.Variable;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class JavaContext extends ResolvingContext {
    private ContextParameters parameters;

    public abstract ObjectValue getThisValue();

    // VARIABLES //
    public Variable getVariable(String name) {
        throw new UnsupportedOperationException();
    }

    public void addVariable(Variable variable) {
        throw new UnsupportedOperationException();
    }

    public void updateVariable(String name, Expression expression) {
        throw new UnsupportedOperationException();
    }

    // FIELDS //
    public Map<String, FieldVar> getStaticFields() {
        return getFields().entrySet().stream()
                .filter(entry -> entry.getValue().isStatic())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void setFields(Map<String, FieldVar> nameToField) {
        getFields().putAll(nameToField);
    }

    public abstract Map<String, FieldVar> getFields();

    // PARAMETERS //
    public void setParameters(ContextParameters parameters) {
        this.parameters = parameters;
    }

    public ContextParameters getParameters() {
        return parameters;
    }
}