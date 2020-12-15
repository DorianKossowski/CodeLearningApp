package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ConstantProvider;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.ForStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.util.exception.ResolvingException;

import java.util.ArrayList;
import java.util.List;

public class ForStmtResolver {

    public static ForStatement resolve(JavaContext context, JavaParser.ForStatementContext forCtx) {
        JavaVisitor<Statement> statementJavaVisitor = context.getVisitor(Statement.class);
        if (forCtx.initExpr != null) {
            statementJavaVisitor.visit(forCtx.initExpr, context);
        }
        List<Statement> contentStatements = new ArrayList<>();
        while (shouldIterate(context, forCtx)) {
            ///BODY
            if (forCtx.updateExpr != null) {
                statementJavaVisitor.visit(forCtx.updateExpr, context);
            }
        }
        return new ForStatement(contentStatements);
    }

    static boolean shouldIterate(JavaContext context, JavaParser.ForStatementContext forCtx) {
        if (forCtx.condExpr != null) {
            return resolveCondition(context, forCtx.condExpr);
        }
        return true;
    }

    static boolean resolveCondition(JavaContext context, JavaParser.ExpressionContext expressionContext) {
        Expression condition = context.getVisitor(Expression.class).visit(expressionContext, context);
        Value value = condition.getValue();
        if (value instanceof ConstantProvider && ((ConstantProvider) value).getConstant().c instanceof Boolean) {
            return (Boolean) ((ConstantProvider) value).getConstant().c;
        }
        throw new ResolvingException(value.getExpression().getText() + " nie jest typu logicznego");
    }
}