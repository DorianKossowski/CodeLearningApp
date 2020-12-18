package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.StatementProperties;
import com.server.parser.java.ast.statement.WhileStatement;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.JavaVisitor;

import java.util.ArrayList;
import java.util.List;

public class WhileStmtResolver extends LoopResolver {

    public static WhileStatement resolve(JavaContext context, JavaParser.WhileStatementContext whileCtx) {
        JavaVisitor<Statement> statementJavaVisitor = context.getVisitor(Statement.class);
        validateLoopContent(context, whileCtx.statement());
        List<Statement> contentStatements = resolveContent(context, whileCtx, statementJavaVisitor);
        return new WhileStatement(contentStatements);
    }

    static List<Statement> resolveContent(JavaContext context, JavaParser.WhileStatementContext whileCtx,
                                          JavaVisitor<Statement> statementJavaVisitor) {
        int iteration = 0;
        List<Statement> contentStatements = new ArrayList<>();
        while (resolveCondition(context, whileCtx.expression())) {
            validateMaxIteration(iteration);
            Statement statement = statementJavaVisitor.visit(whileCtx.statement(), context);
            addIterationProperty(statement, iteration);
            contentStatements.add(statement);
            if (statement.hasBreak()) {
                return contentStatements;
            }
            iteration++;
        }
        return contentStatements;
    }

    private static void addIterationProperty(Statement statement, int iteration) {
        statement.getExpressionStatements()
                .forEach(exprStatement -> exprStatement.addProperty(StatementProperties.WHILE_ITERATION, String.valueOf(iteration)));
    }
}