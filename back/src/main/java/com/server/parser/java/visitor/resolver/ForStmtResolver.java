package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.ForStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.StatementProperties;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.util.exception.ResolvingException;
import org.apache.commons.lang.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

public class ForStmtResolver extends LoopResolver {

    public static ForStatement resolve(JavaContext context, JavaParser.ForStatementContext forCtx) {
        JavaVisitor<Statement> statementJavaVisitor = context.getVisitor(Statement.class);
        if (forCtx.initExpr != null) {
            statementJavaVisitor.visit(forCtx.initExpr, context);
        }
        validateContent(context, forCtx.statement());
        List<Statement> contentStatements = resolveContent(context, forCtx, statementJavaVisitor);
        return new ForStatement(contentStatements);
    }

    static List<Statement> resolveContent(JavaContext context, JavaParser.ForStatementContext forCtx,
                                          JavaVisitor<Statement> statementJavaVisitor) {
        int iteration = 0;
        List<Statement> contentStatements = new ArrayList<>();
        while (shouldIterate(context, forCtx)) {
            validateMaxIteration(iteration);
            Statement statement = statementJavaVisitor.visit(forCtx.statement(), context);
            addIterationProperty(statement, iteration);
            contentStatements.add(statement);
            if (statement.hasBreak()) {
                return contentStatements;
            }
            if (forCtx.updateExpr != null) {
                statementJavaVisitor.visit(forCtx.updateExpr, context);
            }
            iteration++;
        }
        return contentStatements;
    }

    private static void addIterationProperty(Statement statement, int iteration) {
        statement.getExpressionStatements()
                .forEach(exprStatement -> exprStatement.addProperty(StatementProperties.FOR_ITERATION, String.valueOf(iteration)));
    }

    static void validateContent(JavaContext context, JavaParser.StatementContext statementContext) {
        JavaContext validationContext = (JavaContext) SerializationUtils.clone(context);
        JavaVisitor<Statement> visitor = validationContext.getVisitor(Statement.class);
        Statement statement = visitor.visit(statementContext, validationContext);
        if (statement instanceof VariableDef) {
            throw new ResolvingException(String.format("Deklaracja %s nie jest w tym miejscu dozwolona", statement.getText()));
        }
    }

    static boolean shouldIterate(JavaContext context, JavaParser.ForStatementContext forCtx) {
        if (forCtx.condExpr != null) {
            return resolveCondition(context, forCtx.condExpr);
        }
        return true;
    }
}