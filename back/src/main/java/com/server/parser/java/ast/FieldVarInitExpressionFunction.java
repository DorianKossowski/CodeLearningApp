package com.server.parser.java.ast;

import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.context.JavaContext;

import java.util.Objects;
import java.util.function.Function;

public class FieldVarInitExpressionFunction {
    private final String expressionText;
    private final Function<JavaContext, Expression> initFunction;

    public FieldVarInitExpressionFunction(String expressionText, Function<JavaContext, Expression> initFunction) {
        this.expressionText = Objects.requireNonNull(expressionText, "expressionText cannot be null");
        this.initFunction = Objects.requireNonNull(initFunction, "initFunction cannot be null");
    }

    public String getExpressionText() {
        return expressionText;
    }

    public Expression apply(JavaContext context) {
        return initFunction.apply(context);
    }
}