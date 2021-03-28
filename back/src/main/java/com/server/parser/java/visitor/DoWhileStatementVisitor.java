package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.DoWhileStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.property.StatementProperties;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.resolver.util.BreakHandler;
import com.server.parser.java.visitor.resolver.util.ReturnHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.server.parser.java.visitor.resolver.LoopResolver.*;

public class DoWhileStatementVisitor extends JavaVisitor<DoWhileStatement> {
    private final JavaContext context;

    DoWhileStatementVisitor(JavaContext context) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }

    @Override
    public DoWhileStatement visitDoWhileStatement(JavaParser.DoWhileStatementContext ctx) {
        validateLoopContent(context, ctx.statement());
        List<Statement> contentStatements = resolveContent(ctx);
        return new DoWhileStatement(contentStatements);
    }

    List<Statement> resolveContent(JavaParser.DoWhileStatementContext doWhileCtx) {
        int iteration = 0;
        List<Statement> contentStatements = new ArrayList<>();
        do {
            validateMaxIteration(iteration);
            Statement statement = context.resolveStatement(doWhileCtx.statement());
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