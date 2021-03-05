package com.server.parser.java.context;

import com.server.parser.java.JavaVisitorsRegistry;
import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.call.CallResolver;
import com.server.parser.java.value.ObjectValue;
import com.server.parser.java.variable.FieldVar;
import com.server.parser.java.variable.Variable;
import com.server.parser.java.visitor.JavaVisitor;

import java.util.Map;
import java.util.stream.Collectors;

public interface JavaContext extends MethodVerifiable {

    default <T extends AstElement> JavaVisitor<T> getVisitor(Class<T> elementClass) {
        return JavaVisitorsRegistry.get(elementClass);
    }

    default Variable getVariable(String name) {
        throw new UnsupportedOperationException();
    }

    default void addVariable(Variable variable) {
        throw new UnsupportedOperationException();
    }

    default void updateVariable(String name, Expression expression) {
        throw new UnsupportedOperationException();
    }

    default JavaContext createLocalContext() {
        throw new UnsupportedOperationException();
    }

    CallResolver getCallResolver();

    default Map<String, FieldVar> getStaticFields() {
        return getFields().entrySet().stream()
                .filter(entry -> entry.getValue().isStatic())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    default void setFields(Map<String, FieldVar> nameToField) {
        getFields().putAll(nameToField);
    }

    Map<String, FieldVar> getFields();

    String getClassName();

    boolean isStaticContext();

    ObjectValue getThisValue();
}