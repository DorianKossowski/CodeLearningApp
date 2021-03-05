package com.server.parser.java.ast.expression;

import com.server.parser.java.ast.FieldVar;
import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.value.Value;

import java.util.Map;
import java.util.Objects;

public class Instance extends Expression {
    private final String className;
    private final Map<String, FieldVar> nameToField;

    public Instance(String className, Map<String, FieldVar> nameToField) {
        super("instancja " + Objects.requireNonNull(className, "className cannot be null"));
        this.className = className;
        this.nameToField = Objects.requireNonNull(nameToField, "nameToField cannot be null");
    }

    public String getClassName() {
        return className;
    }

    public Map<String, FieldVar> getFields() {
        return nameToField;
    }

    @Override
    public Literal getLiteral() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getResolvedText() {
        return getText();
    }

    @Override
    public String getOutput() {
        return getText();
    }

    @Override
    public Value getValue() {
        return new ObjectValue(this);
    }
}