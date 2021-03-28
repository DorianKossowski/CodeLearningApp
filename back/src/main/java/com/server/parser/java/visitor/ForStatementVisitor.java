package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.ForStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.property.StatementProperties;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.resolver.util.BreakHandler;
import com.server.parser.java.visitor.resolver.util.ReturnHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.server.parser.java.visitor.resolver.ConditionResolver.resolveCondition;
import static com.server.parser.java.visitor.resolver.LoopResolver.*;

public class ForStatementVisitor extends JavaVisitor<ForStatement> {
    private final JavaContext context;

    ForStatementVisitor(JavaContext context) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }

    @Override
    public ForStatement visitForStatement(JavaParser.ForStatementContext ctx) {
        if (ctx.initExpr != null) {
            context.resolveStatement(ctx.initExpr);
        }
        validateLoopContent(context, ctx.statement());
        List<Statement> contentStatements = resolveContent(ctx);
        return new ForStatement(contentStatements);
    }

    List<Statement> resolveContent(JavaParser.ForStatementContext forCtx) {
        int iteration = 0;
        List<Statement> contentStatements = new ArrayList<>();
        while (shouldIterate(forCtx)) {
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

    boolean shouldIterate(JavaParser.ForStatementContext forCtx) {
        if (forCtx.condExpr != null) {
            return resolveCondition(context, forCtx.condExpr);
        }
        return true;
    }
}