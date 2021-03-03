package com.server.parser.java;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.ObjectRef;
import com.server.parser.java.ast.statement.CallStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.Assignment;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.java.visitor.*;

import java.util.Map;
import java.util.function.Supplier;

public class JavaVisitorsRegistry {
    private static final Map<Class<? extends AstElement>, Supplier<JavaVisitor<?>>> visitors =
            ImmutableMap.<Class<? extends AstElement>, Supplier<JavaVisitor<?>>>builder()
                    .put(VariableDef.class, Suppliers.memoize(VariableDefVisitor::new))
                    .put(Expression.class, Suppliers.memoize(ExpressionVisitor::new))
                    .put(Method.class, Suppliers.memoize(MethodVisitor::new))
                    .put(ClassAst.class, Suppliers.memoize(ClassVisitor::new))
                    .put(Statement.class, Suppliers.memoize(StatementVisitor::new))
                    .put(CallStatement.class, Suppliers.memoize(CallStatementVisitor::new))
                    // TODO create ObjectRefVisitor
                    .put(ObjectRef.class, Suppliers.memoize(ExpressionVisitor::new))
                    .put(Assignment.class, Suppliers.memoize(AssignmentStatementVisitor::new))
                    .build();

    public static <T extends AstElement> JavaVisitor<T> get(Class<T> elementClass) {
        JavaVisitor<?> visitor = visitors.get(elementClass).get();
        if (visitor == null) {
            throw new RuntimeException(String.format("Visitor for class %s not exists", elementClass));
        }
        return (JavaVisitor<T>) visitor;
    }
}