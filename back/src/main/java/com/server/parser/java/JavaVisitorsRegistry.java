package com.server.parser.java;

import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.java.visitor.*;

import java.util.HashMap;
import java.util.Map;

public class JavaVisitorsRegistry {
    private static final Map<Class<? extends AstElement>, JavaVisitor<?>> visitors = new HashMap<>();

    static {
        visitors.put(VariableDef.class, new StatementVisitor());
        visitors.put(Expression.class, new ExpressionVisitor());
        visitors.put(Method.class, new MethodVisitor());
        visitors.put(ClassAst.class, new ClassVisitor());
    }

    public static <T extends AstElement> JavaVisitor<T> get(Class<T> elementClass) {
        JavaVisitor<?> visitor = visitors.get(elementClass);
        if (visitor == null) {
            throw new RuntimeException(String.format("Visitor for class %s not exists", elementClass));
        }
        return (JavaVisitor<T>) visitor;
    }
}