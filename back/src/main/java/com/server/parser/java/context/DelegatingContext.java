package com.server.parser.java.context;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.value.Value;
import com.server.parser.java.value.util.ValuePreparer;
import com.server.parser.java.variable.FieldVar;
import com.server.parser.java.variable.Variable;
import com.server.parser.util.exception.ResolvingException;

import java.util.Map;
import java.util.Objects;

public abstract class DelegatingContext extends JavaContext {
    private final JavaContext context;

    public DelegatingContext(JavaContext context) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
        setParameters(context.getParameters());
    }

    public abstract JavaContext createLocalContext();

    @Override
    public ObjectValue getThisValue() {
        return context.getThisValue();
    }

    @Override
    public Variable getVariable(String name) {
        return context.getVariable(name);
    }

    @Override
    public void addVariable(Variable variable) {
        context.addVariable(variable);
    }

    @Override
    public void updateVariable(String name, Expression expression) {
        Variable variable = getVariable(name);
        Value newValue = ValuePreparer.prepare(variable.getType(), expression);
        variable.setValue(newValue);
    }

    @Override
    public Map<String, FieldVar> getFields() {
        return context.getFields();
    }

    Map<String, Variable> getImmutableVariables() {
        return ((DelegatingContext) context).getImmutableVariables();
    }

    String getValidatedVariableName(Variable var) {
        String varName = var.getName();
        if (getImmutableVariables().containsKey(varName)) {
            throw new ResolvingException("Obiekt " + varName + " już istnieje");
        }
        return varName;
    }
}