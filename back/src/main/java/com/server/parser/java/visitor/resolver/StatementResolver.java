package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ConstantProvider;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.JavaContext;
import com.server.parser.util.exception.ResolvingException;

public class StatementResolver {

    protected static boolean resolveCondition(JavaContext context, JavaParser.ExpressionContext expressionContext) {
        Expression condition = context.getVisitor(Expression.class).visit(expressionContext, context);
        Value value = condition.getValue();
        if (value instanceof ConstantProvider && ((ConstantProvider) value).getConstant().c instanceof Boolean) {
            return (Boolean) ((ConstantProvider) value).getConstant().c;
        }
        throw new ResolvingException(value.getExpression().getText() + " nie jest typu logicznego");
    }
}