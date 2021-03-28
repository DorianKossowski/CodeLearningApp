package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.ForStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.property.StatementProperties;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.resolver.util.BreakHandler;
import com.server.parser.java.visitor.resolver.util.ReturnHandler;

import java.util.ArrayList;
import java.util.List;

public class ForStmtResolver extends LoopResolver {

    public static ForStatement resolve(JavaContext context, JavaParser.ForStatementContext forCtx) {
        if (forCtx.initExpr != null) {
            context.resolveStatement(forCtx.initExpr);
        }
        validateLoopContent(context, forCtx.statement());
        List<Statement> contentStatements = resolveContent(context, forCtx);
        return new ForStatement(contentStatements);
    }

    static List<Statement> resolveContent(JavaContext context, JavaParser.ForStatementContext forCtx) {
        int iteration = 0;
        List<Statement> contentStatements = new ArrayList<>();
        while (shouldIterate(context, forCtx)) {
            validateMaxIteration(iteration);
            Statement statement = context.resolveStatement(forCtx.statement());
            addIterationProperty(statement, StatementProperties.FOR_ITERATION, iteration);
            contentStatements.add(statement);
            if (ReturnHandler.shouldReturn(statement) || BreakHandler.shouldBreak(statement)) {
                return contentStatements;
            }
            if (forCtx.updateExpr != null) {
                context.resolveStatement(forCtx.updateExpr);
            }
            iteration++;
        }
        return contentStatements;
    }

    static boolean shouldIterate(JavaContext context, JavaParser.ForStatementContext forCtx) {
        if (forCtx.condExpr != null) {
            return resolveCondition(context, forCtx.condExpr);
        }
        return true;
    }
}