package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ConstantProvider;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.SwitchStatement;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.StatementListVisitor;
import com.server.parser.util.exception.ResolvingException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SwitchStmtResolver {
    private static final String EXCEPTION_SUFFIX = " nie jest jednego z typów: char, byte, short, int, Character, " +
            "Byte, Short, Integer, String";
    private final JavaContext context;
    private final StatementListVisitor statementListVisitor = new StatementListVisitor();

    public SwitchStmtResolver(JavaContext context) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }

    public Statement resolve(JavaParser.SwitchStatementContext switchCtx) {
        Value value = resolveExpression(switchCtx.expression());
        List<SwitchElement> switchElements = switchCtx.switchElement().stream()
                .map(this::resolveSwitchElement)
                .collect(Collectors.toList());
        return new SwitchStatement(null);
    }

    SwitchElement resolveSwitchElement(JavaParser.SwitchElementContext switchElementContext) {
        List<Expression> labelExpressions = resolveLabelExpressions(switchElementContext.switchElementLabel());
        List<Statement> statements = statementListVisitor.visit(switchElementContext.statementList(), context);
        return new SwitchElement(labelExpressions, statements);
    }

    List<Expression> resolveLabelExpressions(List<JavaParser.SwitchElementLabelContext> switchElementLabelContexts) {
        return switchElementLabelContexts.stream().map(switchElementLabelContext -> {
            if (switchElementLabelContext.CASE() != null) {
                return context.getVisitor(Expression.class).visit(switchElementLabelContext.expression(), context);
            }
            return null;
        }).collect(Collectors.toList());
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

    static class SwitchElement {
        private final List<Expression> labelExpressions;
        private final List<Statement> statements;

        private SwitchElement(List<Expression> labelExpressions, List<Statement> statements) {
            this.labelExpressions = Objects.requireNonNull(labelExpressions, "labelExpressions cannot be null");
            this.statements = Objects.requireNonNull(statements, "statements cannot be null");
        }

        public List<Expression> getLabelExpressions() {
            return labelExpressions;
        }

        public List<Statement> getStatements() {
            return statements;
        }
    }
}