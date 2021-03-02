package com.server.parser.java.ast;

import com.server.parser.java.ast.expression.Expression;

import java.util.Objects;
import java.util.function.Supplier;

public class FieldVarInitExpressionSupplier {
    private final Supplier<Expression> expressionSupplier;

    public FieldVarInitExpressionSupplier(Supplier<Expression> expressionSupplier) {
        this.expressionSupplier = Objects.requireNonNull(expressionSupplier, "expressionSupplier cannot be null");
    }

    public Expression get() {
        return expressionSupplier.get();
    }
}