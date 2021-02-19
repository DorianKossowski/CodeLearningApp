package com.server.parser.java.ast.statement.expression_statement;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.value.Value;
import com.server.parser.util.ValuePreparer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class VariableDef extends ExpressionStatement implements MethodPrintable {
    private List<String> modifiers = new ArrayList<>();
    private final String type;
    private final String name;
    private final Expression expression;
    private final Value value;
    private final boolean explicitInitialization;
    private String contextMethodName;

    public VariableDef(String text, String type, String name, Expression expression, boolean explicitInitialization) {
        super(text);
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.expression = Objects.requireNonNull(expression, "expression cannot be null");
        this.value = ValuePreparer.prepare(type, expression);
        this.explicitInitialization = explicitInitialization;
    }

    public void setModifiers(List<String> modifiers) {
        this.modifiers = modifiers;
    }

    public List<String> getModifiers() {
        return modifiers;
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

    public void setContextMethodName(String contextMethodName) {
        this.contextMethodName = contextMethodName;
    }

    @Override
    public String getResolved() {
        StringBuilder text = new StringBuilder().append(String.format("%s %s %s", String.join(" ", modifiers), type,
                name));
        if (explicitInitialization) {
            text.append(" = ").append(value);
        }
        return text.toString();
    }

    @Override
    public String printMethodName() {
        return Optional.ofNullable(contextMethodName).orElse("");
    }
}