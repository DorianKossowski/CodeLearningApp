package com.server.parser.java.context;

import com.server.parser.java.JavaVisitorsRegistry;
import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.FieldVar;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.call.CallResolver;
import com.server.parser.java.visitor.JavaVisitor;

import java.util.Map;

public interface JavaContext extends MethodVerifiable {

    default <T extends AstElement> JavaVisitor<T> getVisitor(Class<T> elementClass) {
        return JavaVisitorsRegistry.get(elementClass);
    }

    default void addField(FieldVar fieldVar) {
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

    CallResolver getCallResolver();

    Map<String, FieldVar> getStaticFields();

    void setFields(Map<String, FieldVar> nameToField);

    String getClassName();

    boolean isStaticContext();
}