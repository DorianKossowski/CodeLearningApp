package com.server.parser.java.ast.statement;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.value.Value;
import com.server.parser.util.EmptyExpressionPreparer;
import com.server.parser.util.ValuePreparer;

import java.util.Objects;

public class VariableDef extends Statement {
    private final String type;
    private final String name;
    private final Expression expression;
    private final Value value;

    public VariableDef(String text, String type, String name) {
        this(text, type, name, EmptyExpressionPreparer.prepare(type));
    }

    public VariableDef(String text, String type, String name, Expression expression) {
        super(text);
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.expression = Objects.requireNonNull(expression, "expression cannot be null");
        this.value = ValuePreparer.prepare(type, expression);
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
        return String.format("%s %s = %s", type, name, value);
    }
}