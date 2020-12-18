package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.util.exception.ResolvingException;
import org.apache.commons.lang.SerializationUtils;

public class LoopResolver extends StatementResolver {
    static final int MAX_ITER_NUMBER = 1000;

    static void validateMaxIteration(int iterNumber) {
        if (iterNumber >= MAX_ITER_NUMBER) {
            throw new ResolvingException("Ogranicz liczbę iteracji! Maksymalna dostępna liczba iteracji w pętli to 1000");
        }
    }

    static void validateLoopContent(JavaContext context, JavaParser.StatementContext statementContext) {
        JavaContext validationContext = (JavaContext) SerializationUtils.clone(context);
        JavaVisitor<Statement> visitor = validationContext.getVisitor(Statement.class);
        Statement statement = visitor.visit(statementContext, validationContext);
        if (statement instanceof VariableDef) {
            throw new ResolvingException(String.format("Deklaracja %s nie jest w tym miejscu dozwolona", statement.getText()));
        }
    }
}