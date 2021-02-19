package com.server.parser.java.ast.statement.expression_statement;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.value.Value;
import com.server.parser.util.ValuePreparer;

import java.util.Objects;

public class FieldVarDef extends VariableDef {
    private final Expression expression;
    private final Value value;
    private final boolean explicitInitialization;

    public FieldVarDef(String text, String type, String name, Expression expression, boolean explicitInitialization) {
        super(text, type, name);
        this.expression = Objects.requireNonNull(expression, "expression cannot be null");
        this.value = ValuePreparer.prepare(type, expression);
        this.explicitInitialization = explicitInitialization;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public Value getValue() {
        return value;
    }

    @Override
    public String getResolved() {
        StringBuilder text = new StringBuilder().append(String.format("%s %s %s", String.join(" ", getModifiers()),
                getType(), getName()));
        if (explicitInitialization) {
            text.append(" = ").append(value);
        }
        return text.toString();
    }
}