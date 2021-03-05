package com.server.parser.java.context;

import com.server.parser.java.JavaVisitorsRegistry;
import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.variable.FieldVar;
import com.server.parser.java.variable.Variable;
import com.server.parser.java.visitor.JavaVisitor;

import java.util.Map;
import java.util.stream.Collectors;

public abstract class JavaContext {
    private ContextParameters parameters;

    public <T extends AstElement> JavaVisitor<T> getVisitor(Class<T> elementClass) {
        return JavaVisitorsRegistry.get(elementClass);
    }

    public Variable getVariable(String name) {
        throw new UnsupportedOperationException();
    }

    public void addVariable(Variable variable) {
        throw new UnsupportedOperationException();
    }

    public void updateVariable(String name, Expression expression) {
        throw new UnsupportedOperationException();
    }

    public JavaContext createLocalContext() {
        throw new UnsupportedOperationException();
    }

    public Map<String, FieldVar> getStaticFields() {
        return getFields().entrySet().stream()
                .filter(entry -> entry.getValue().isStatic())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void setFields(Map<String, FieldVar> nameToField) {
        getFields().putAll(nameToField);
    }

    public abstract Map<String, FieldVar> getFields();

    public abstract ObjectValue getThisValue();

    public void setParameters(ContextParameters parameters) {
        this.parameters = parameters;
    }

    public ContextParameters getParameters() {
        return parameters;
    }
}