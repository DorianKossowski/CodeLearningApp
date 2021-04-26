package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.WhileStatement;
import com.server.parser.java.ast.statement.property.StatementProperties;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.resolver.util.BreakHandler;
import com.server.parser.java.visitor.resolver.util.ReturnHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.server.parser.java.visitor.resolver.ConditionResolver.resolveCondition;
import static com.server.parser.java.visitor.resolver.LoopResolver.*;

public class WhileStatementVisitor extends JavaVisitor<WhileStatement> {
    private final JavaContext context;

    WhileStatementVisitor(JavaContext context) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }

    @Override
    public WhileStatement visitWhileStatement(JavaParser.WhileStatementContext ctx) {
        validateLoopContent(context, ctx.statement());
        List<Statement> contentStatements = resolveContent(ctx);
        return new WhileStatement(contentStatements);
    }

    List<Statement> resolveContent(JavaParser.WhileStatementContext whileCtx) {
        int iteration = 0;
        List<Statement> contentStatements = new ArrayList<>();
        while (resolveCondition(context, whileCtx.expression())) {
            validateMaxIteration(iteration);
            Statement statement = context.resolveStatement(whileCtx.statement());
            addIterationProperty(statement, StatementProperties.WHILE_ITERATION, iteration);
            contentStatements.add(statement);
            if (ReturnHandler.shouldReturn(statement) || BreakHandler.shouldBreak(statement)) {
                return contentStatements;
            }
            iteration++;
        }
        return contentStatements;
    }
}