package com.server.parser.java;

import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.Expression;
import com.server.parser.java.ast.Literal;
import com.server.parser.java.ast.Variable;
import com.server.parser.java.visitor.ExpressionVisitor;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.java.visitor.LiteralVisitor;
import com.server.parser.java.visitor.VariableVisitor;

import java.util.HashMap;
import java.util.Map;

public class JavaVisitorsRegistry {
    private static final Map<Class<? extends AstElement>, JavaVisitor<?>> visitors = new HashMap<>();

    static {
        visitors.put(Variable.class, new VariableVisitor());
        visitors.put(Expression.class, new ExpressionVisitor());
        visitors.put(Literal.class, new LiteralVisitor());
    }

    public static <T extends AstElement> JavaVisitor<T> get(Class<T> elementClass) {
        JavaVisitor<?> visitor = visitors.get(elementClass);
        if (visitor == null) {
            throw new RuntimeException(String.format("Visitor for class %s not exists", elementClass));
        }
        return (JavaVisitor<T>) visitor;
    }
}