package com.server.parser.java.context;

import com.server.parser.java.JavaVisitorsRegistry;
import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.visitor.JavaVisitor;

import java.io.Serializable;

public interface JavaContext extends Serializable {

    default <T extends AstElement> JavaVisitor<T> getVisitor(Class<T> elementClass) {
        return JavaVisitorsRegistry.get(elementClass);
    }

    default void addField(Variable variable) {
        throw new UnsupportedOperationException();
    }

    Variable getVariable(String name);

    void addVariable(Variable variable);

    void updateVariable(String name, Expression expression);

    // TODO make only for MethodContext
    String getMethodName();

    JavaContext createLocalContext();
}