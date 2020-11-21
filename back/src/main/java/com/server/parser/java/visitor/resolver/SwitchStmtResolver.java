package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ConstantProvider;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.SwitchStatement;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.StatementVisitor;
import com.server.parser.util.exception.ResolvingException;

import java.util.Objects;

public class SwitchStmtResolver {
    private static final String EXCEPTION_SUFFIX = " nie jest jednego z typów: char, byte, short, int, Character, " +
            "Byte, Short, Integer, String";
    private final JavaContext context;
    private final StatementVisitor.StatementVisitorInternal statementVisitor;

    public SwitchStmtResolver(JavaContext context, StatementVisitor.StatementVisitorInternal statementVisitor) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
        this.statementVisitor = Objects.requireNonNull(statementVisitor, "statementVisitor cannot be null");
    }

    public Statement resolve(JavaParser.SwitchStatementContext switchCtx) {
        resolveExpression(switchCtx.expression());
        return new SwitchStatement();
    }

    Value resolveExpression(JavaParser.ExpressionContext expressionContext) {
        Expression expression = context.getVisitor(Expression.class).visit(expressionContext, context);
        Value value = expression.getValue();
        if (!(value instanceof ConstantProvider)) {
            throw new ResolvingException(value.getExpression().getText() + EXCEPTION_SUFFIX);
        }
        Class<?> valueClass = ((ConstantProvider) value).getConstant().c.getClass();
        if (valueClass.equals(Character.class) || valueClass.equals(Integer.class) || valueClass.equals(String.class)) {
            return value;
        }
        throw new ResolvingException(value.getExpression().getText() + EXCEPTION_SUFFIX);
    }
}