package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.ContextCopyFactory;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.util.exception.ResolvingException;

public class LoopResolver extends StatementResolver {
    static final int MAX_ITER_NUMBER = 1000;

    static void validateMaxIteration(int iterNumber) {
        if (iterNumber >= MAX_ITER_NUMBER) {
            throw new ResolvingException("Ogranicz liczbę iteracji! Maksymalna dostępna liczba iteracji w pętli to 1000");
        }
    }

    static void validateLoopContent(JavaContext context, JavaParser.StatementContext statementContext) {
        JavaContext validationContext = ContextCopyFactory.createValidationContext(context);
        JavaVisitor<Statement> visitor = validationContext.getVisitor(Statement.class);
        Statement statement = visitor.visit(statementContext, validationContext);
        if (statement instanceof VariableDef) {
            throw new ResolvingException(String.format("Deklaracja %s nie jest w tym miejscu dozwolona", statement.getText()));
        }
    }

    static void addIterationProperty(Statement statement, String propertyName, int iteration) {
        statement.getExpressionStatements()
                .forEach(exprStatement -> exprStatement.addProperty(propertyName, String.valueOf(iteration)));
    }
}