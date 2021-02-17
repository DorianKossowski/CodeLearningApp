package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.resolver.util.BreakHandler;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatementListVisitor extends JavaVisitor<List<Statement>> {

    @Override
    public List<Statement> visit(ParserRuleContext ctx, JavaContext context) {
        return new StatementListVisitorInternal(context).visit(ctx);
    }

    public static class StatementListVisitorInternal extends JavaBaseVisitor<List<Statement>> {
        private final JavaContext context;

        private StatementListVisitorInternal(JavaContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

        @Override
        public List<Statement> visitStatementList(JavaParser.StatementListContext ctx) {
            JavaVisitor<Statement> statementVisitor = context.getVisitor(Statement.class);

            List<Statement> statements = new ArrayList<>();
            for (int i = 0; i < ctx.getChildCount(); ++i) {
                ParseTree child = ctx.getChild(i);
                if (child instanceof ParserRuleContext) {
                    Statement statement = statementVisitor.visit((ParserRuleContext) child, context);
                    statements.add(statement);
                    if (BreakHandler.shouldBreak(statement)) {
                        return statements;
                    }
                }
            }
            return statements;
        }
    }
}