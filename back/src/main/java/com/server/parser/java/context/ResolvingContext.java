package com.server.parser.java.context;

import com.server.parser.java.ast.AstElement;
import com.server.parser.java.ast.Statements;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.java.visitor.JavaVisitorsRegistry;
import org.antlr.v4.runtime.tree.ParseTree;

public abstract class ResolvingContext {

    public <T extends AstElement> JavaVisitor<T> getVisitor(Class<T> elementClass, JavaContext context) {
        return JavaVisitorsRegistry.get(elementClass, context);
    }

    public Expression resolveExpression(ParseTree expressionContext) {
        JavaVisitor<Expression> visitor = getVisitor(Expression.class, (JavaContext) this);
        return visitor.visit(expressionContext);
    }

    public Statement resolveStatement(ParseTree statementContext) {
        JavaVisitor<Statement> visitor = getVisitor(Statement.class, (JavaContext) this);
        return visitor.visit(statementContext);
    }

    public Statements resolveStatements(ParseTree statementsContext) {
        JavaVisitor<Statements> visitor = getVisitor(Statements.class, (JavaContext) this);
        return visitor.visit(statementsContext);
    }
}