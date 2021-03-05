package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.DoWhileStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.property.StatementProperties;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.java.visitor.resolver.util.BreakHandler;
import com.server.parser.java.visitor.resolver.util.ReturnHandler;

import java.util.ArrayList;
import java.util.List;

public class DoWhileStmtResolver extends LoopResolver {

    public static DoWhileStatement resolve(JavaContext context, JavaParser.DoWhileStatementContext doWhileCtx) {
        JavaVisitor<Statement> statementJavaVisitor = context.getVisitor(Statement.class);
        validateLoopContent(context, doWhileCtx.statement());
        List<Statement> contentStatements = resolveContent(context, doWhileCtx, statementJavaVisitor);
        return new DoWhileStatement(contentStatements);
    }

    static List<Statement> resolveContent(JavaContext context, JavaParser.DoWhileStatementContext doWhileCtx,
                                          JavaVisitor<Statement> statementJavaVisitor) {
        int iteration = 0;
        List<Statement> contentStatements = new ArrayList<>();
        do {
            validateMaxIteration(iteration);
            Statement statement = statementJavaVisitor.visit(doWhileCtx.statement(), context);
            addIterationProperty(statement, StatementProperties.DO_WHILE_ITERATION, iteration);
            contentStatements.add(statement);
            if (ReturnHandler.shouldReturn(statement) || BreakHandler.shouldBreak(statement)) {
                return contentStatements;
            }
            iteration++;
        } while (resolveCondition(context, doWhileCtx.expression()));
        return contentStatements;
    }
}