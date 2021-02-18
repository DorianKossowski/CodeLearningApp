package com.server.parser.java.ast.statement;

import com.server.parser.java.ast.expression.Expression;

import java.util.Objects;

public class ReturnStatement extends ExpressionStatement {
    private final Expression expression;

    public ReturnStatement(String text, Expression expression) {
        super(text);
        this.expression = Objects.requireNonNull(expression, "expression cannot be null");
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String getResolved() {
        String expressionStr = expression.getResolvedText();
        return String.format("return%s", expressionStr.isEmpty() ? "" : " " + expressionStr);
    }
}