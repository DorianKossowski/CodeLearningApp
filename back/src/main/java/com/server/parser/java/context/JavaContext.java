package com.server.parser.java.context;

import com.server.parser.java.JavaVisitorsRegistry;
import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.call.CallHandler;
import com.server.parser.java.visitor.JavaVisitor;

import java.util.Map;

public interface JavaContext extends MethodVerifiable {

    default <T extends AstElement> JavaVisitor<T> getVisitor(Class<T> elementClass) {
        return JavaVisitorsRegistry.get(elementClass);
    }

    default void addField(Variable variable) {
        throw new UnsupportedOperationException();
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

    CallHandler getCallHandler();

    Map<String, Variable> getStaticFields();

    void setStaticFields(Map<String, Variable> nameToField);
}