package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.Statements;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.context.ContextFactory;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.resolver.util.BreakHandler;
import com.server.parser.java.visitor.resolver.util.ReturnHandler;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatementsVisitor extends JavaVisitor<Statements> {
    private final JavaContext context;

    StatementsVisitor(JavaContext context) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }

    @Override
    public Statements visitStatements(JavaParser.StatementsContext ctx) {
        return visitStartingFromChild(ctx, context, 0);
    }

    private Statements visitStartingFromChild(JavaParser.StatementsContext ctx, JavaContext context, int startingChild) {
        List<Statement> statements = new ArrayList<>();
        for (int i = startingChild; i < ctx.getChildCount(); ++i) {
            ParseTree child = ctx.getChild(i);
            Statement statement = context.resolveStatement(child);
            statements.add(statement);
            if (ReturnHandler.shouldReturn(statement) || BreakHandler.shouldBreak(statement)) {
                validateRemainingStatements(ctx, context, i + 1);
                return new Statements(statements);
            }
        }
        return new Statements(statements);
    }

    private void validateRemainingStatements(JavaParser.StatementsContext ctx, JavaContext context, int startingChild) {
        JavaContext validationContext = ContextFactory.createValidationContext(context);
        visitStartingFromChild(ctx, validationContext, startingChild);
    }
}