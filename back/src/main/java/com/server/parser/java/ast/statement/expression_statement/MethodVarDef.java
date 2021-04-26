package com.server.parser.java.ast.statement.expression_statement;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.value.Value;
import com.server.parser.java.value.util.ValuePreparer;

import java.util.Optional;

public class MethodVarDef extends VariableDef implements MethodPrintable {
    private final Value value;
    private String contextMethodName;

    public MethodVarDef(String text, String type, String name, Expression expression, boolean explicitInitialization) {
        super(text, type, name, explicitInitialization);
        this.value = ValuePreparer.prepare(type, expression);
    }

    @Override
    public Value getValue() {
        return value;
    }

    public void setContextMethodName(String contextMethodName) {
        this.contextMethodName = contextMethodName;
    }

    @Override
    public String printMethodName() {
        return Optional.ofNullable(contextMethodName).orElse("");
    }
}