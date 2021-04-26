package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.ContextFactory;
import com.server.parser.java.context.JavaContext;
import com.server.parser.util.exception.ResolvingException;

public final class LoopResolver {
    static final int MAX_ITER_NUMBER = 1000;

    public static void validateMaxIteration(int iterNumber) {
        if (iterNumber >= MAX_ITER_NUMBER) {
            throw new ResolvingException("Ogranicz liczbę iteracji! Maksymalna dostępna liczba iteracji w pętli to 1000");
        }
    }

    public static void validateLoopContent(JavaContext context, JavaParser.StatementContext statementContext) {
        JavaContext validationContext = ContextFactory.createValidationContext(context);
        Statement statement = validationContext.resolveStatement(statementContext);
        if (statement instanceof VariableDef) {
            throw new ResolvingException(String.format("Deklaracja %s nie jest w tym miejscu dozwolona", statement.getText()));
        }
    }

    public static void addIterationProperty(Statement statement, String propertyName, int iteration) {
        statement.getExpressionStatements()
                .forEach(exprStatement -> exprStatement.addProperty(propertyName, String.valueOf(iteration)));
    }
}