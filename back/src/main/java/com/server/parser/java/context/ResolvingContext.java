package com.server.parser.java.context;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.java.visitor.JavaVisitorsRegistry;
import com.server.parser.java.visitor.StatementListVisitor;

import java.util.List;

public abstract class ResolvingContext {

    public <T extends AstElement> JavaVisitor<T> getVisitor(Class<T> elementClass, JavaContext context) {
        return JavaVisitorsRegistry.get(elementClass, context);
    }

    public Expression resolveExpression(JavaContext context, JavaParser.ExpressionContext expressionContext) {
        JavaVisitor<Expression> visitor = getVisitor(Expression.class, context);
        return visitor.visit(expressionContext);
    }

    public List<Statement> resolveStatements(JavaContext context, JavaParser.StatementListContext statementsContext) {
        StatementListVisitor statementListVisitor = new StatementListVisitor(context);
        return statementListVisitor.visit(statementsContext);
    }
}