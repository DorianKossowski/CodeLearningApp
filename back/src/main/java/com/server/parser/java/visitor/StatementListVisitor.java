package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.context.ContextFactory;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.resolver.util.BreakHandler;
import com.server.parser.java.visitor.resolver.util.ReturnHandler;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatementListVisitor extends JavaVisitor<List<Statement>> {
    private final JavaContext context;

    public StatementListVisitor(JavaContext context) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }

    @Override
    public List<Statement> visitStatementList(JavaParser.StatementListContext ctx) {
        return visitStartingFromChild(ctx, context, 0);
    }

    private List<Statement> visitStartingFromChild(JavaParser.StatementListContext ctx, JavaContext context, int startingChild) {
        JavaVisitor<Statement> statementVisitor = context.getVisitor(Statement.class, context);
        List<Statement> statements = new ArrayList<>();
        for (int i = startingChild; i < ctx.getChildCount(); ++i) {
            ParseTree child = ctx.getChild(i);
            Statement statement = statementVisitor.visit(child);
            statements.add(statement);
            if (ReturnHandler.shouldReturn(statement) || BreakHandler.shouldBreak(statement)) {
                validateRemainingStatements(ctx, context, i + 1);
                return statements;
            }
        }
        return statements;
    }

    private void validateRemainingStatements(JavaParser.StatementListContext ctx, JavaContext context, int startingChild) {
        JavaContext validationContext = ContextFactory.createValidationContext(context);
        visitStartingFromChild(ctx, validationContext, startingChild);
    }
}