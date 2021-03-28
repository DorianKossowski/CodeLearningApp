package com.server.parser.java.visitor;

import com.google.common.collect.ImmutableMap;
import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.ClassAst;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.expression.ObjectRefExpression;
import com.server.parser.java.ast.statement.*;
import com.server.parser.java.ast.statement.expression_statement.Assignment;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.JavaContext;

import java.util.Map;
import java.util.function.Function;

public class JavaVisitorsRegistry {
    private static final Map<Class<? extends AstElement>, Function<JavaContext, JavaVisitor<?>>> visitors =
            ImmutableMap.<Class<? extends AstElement>, Function<JavaContext, JavaVisitor<?>>>builder()
                    .put(VariableDef.class, VariableDefVisitor::new)
                    .put(Statement.class, StatementVisitor::new)
                    .put(ObjectRefExpression.class, ObjectRefExpressionVisitor::new)
                    .put(Method.class, MethodVisitor::new)
                    .put(Expression.class, ExpressionVisitor::new)
                    .put(ClassAst.class, ClassVisitor::new)
                    .put(CallStatement.class, CallStatementVisitor::new)
                    .put(Assignment.class, AssignmentStatementVisitor::new)
                    .put(IfElseStatement.class, IfElseStatementVisitor::new)
                    .put(DoWhileStatement.class, DoWhileStatementVisitor::new)
                    .put(WhileStatement.class, WhileStatementVisitor::new)
                    .put(ForStatement.class, ForStatementVisitor::new)
                    .put(SwitchStatement.class, SwitchStatementVisitor::new)
                    .build();

    public static <T extends AstElement> JavaVisitor<T> get(Class<T> elementClass, JavaContext context) {
        JavaVisitor<?> visitor = visitors.get(elementClass).apply(context);
        if (visitor == null) {
            throw new RuntimeException(String.format("Visitor for class %s not exists", elementClass));
        }
        return (JavaVisitor<T>) visitor;
    }
}