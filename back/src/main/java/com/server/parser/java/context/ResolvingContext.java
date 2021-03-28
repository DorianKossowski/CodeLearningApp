package com.server.parser.java.context;

import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.java.visitor.JavaVisitorsRegistry;
import com.server.parser.java.visitor.StatementListVisitor;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public abstract class ResolvingContext {

    public <T extends AstElement> JavaVisitor<T> getVisitor(Class<T> elementClass, JavaContext context) {
        return JavaVisitorsRegistry.get(elementClass, context);
    }

    public Expression resolveExpression(JavaContext context, ParseTree expressionContext) {
        JavaVisitor<Expression> visitor = getVisitor(Expression.class, context);
        return visitor.visit(expressionContext);
    }

    public Statement resolveStatement(JavaContext context, ParseTree statementContext) {
        JavaVisitor<Statement> visitor = getVisitor(Statement.class, context);
        return visitor.visit(statementContext);
    }

    public List<Statement> resolveStatements(JavaContext context, ParseTree statementsContext) {
        StatementListVisitor visitor = new StatementListVisitor(context);
        return visitor.visit(statementsContext);
    }
}