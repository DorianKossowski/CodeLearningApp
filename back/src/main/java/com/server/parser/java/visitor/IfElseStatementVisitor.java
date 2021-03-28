package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.IfElseStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.ContextFactory;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.resolver.StatementResolver;
import com.server.parser.util.exception.ResolvingException;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Objects;

public class IfElseStatementVisitor extends JavaVisitor<IfElseStatement> {
    private final JavaContext context;

    public IfElseStatementVisitor(JavaContext context) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
    }

    @Override
    public IfElseStatement visit(ParserRuleContext ctx, JavaContext context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IfElseStatement visitIfElseStatement(JavaParser.IfElseStatementContext ctx) {
        validateBranchesContent(ctx);
        boolean condValue = StatementResolver.resolveCondition(context, ctx.cond);
        Statement visitedStatement = null;
        JavaVisitor<Statement> statementVisitor = context.getVisitor(Statement.class, context);
        if (condValue) {
            visitedStatement = statementVisitor.visit(ctx.statement(0));
        } else if (ctx.statement(1) != null) {
            return IfElseStatement.createElse(statementVisitor.visit(ctx.statement(1)));
        }
        return IfElseStatement.createIf(ctx.cond.getText(), visitedStatement);
    }

    void validateBranchesContent(JavaParser.IfElseStatementContext ctx) {
        JavaContext validationContext = ContextFactory.createValidationContext(context);
        JavaVisitor<Statement> visitor = validationContext.getVisitor(Statement.class, validationContext);

        Statement ifContentStmt = visitor.visit(ctx.statement(0));
        validateBranchContent(ifContentStmt);
        if (ctx.statement(1) != null) {
            Statement elseContentStmt = visitor.visit(ctx.statement(1));
            validateBranchContent(elseContentStmt);
        }
    }

    void validateBranchContent(Statement ifContentStmt) {
        if (ifContentStmt instanceof VariableDef) {
            throw new ResolvingException(String.format("Deklaracja %s nie jest w tym miejscu dozwolona",
                    ifContentStmt.getText()));
        }
    }
}