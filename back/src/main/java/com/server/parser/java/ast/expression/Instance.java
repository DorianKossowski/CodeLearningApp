package com.server.parser.java.ast.expression;

import com.server.parser.java.ast.FieldVar;
import com.server.parser.java.ast.constant.Constant;
import com.server.parser.java.ast.value.Value;

import java.util.Map;
import java.util.Objects;

public class Instance extends Expression {
    private final Map<String, FieldVar> nameToField;

    public Instance(String className, Map<String, FieldVar> nameToField) {
        super("instancja " + className);
        this.nameToField = Objects.requireNonNull(nameToField, "nameToField cannot be null");
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
        throw new UnsupportedOperationException();
    }

    @Override
    public Constant<?> getConstant() {
        throw new UnsupportedOperationException();
    }
}