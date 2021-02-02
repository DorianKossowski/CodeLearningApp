package com.server.parser.java.ast.statement;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.value.Value;
import com.server.parser.util.ValuePreparer;

import java.util.Objects;

public class VariableDef extends ExpressionStatement {
    private final String type;
    private final String name;
    private final Expression expression;
    private final Value value;
    private final boolean explicitInitialization;

    public VariableDef(String text, String type, String name, Expression expression, boolean explicitInitialization) {
        super(text);
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.expression = Objects.requireNonNull(expression, "expression cannot be null");
        this.value = ValuePreparer.prepare(type, expression);
        this.explicitInitialization = explicitInitialization;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Expression getExpression() {
        return expression;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String getResolved() {
        StringBuilder text = new StringBuilder().append(String.format("%s %s", type, name));
        if (explicitInitialization) {
            text.append(" = ").append(value);
        }
        return text.toString();
    }
}