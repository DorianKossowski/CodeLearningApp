package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ConstantProvider;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.IfElseStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.StatementVisitor;
import com.server.parser.util.exception.ResolvingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IfStmtResolver {
    private final JavaContext context;
    private final StatementVisitor.StatementVisitorInternal statementVisitor;

    public IfStmtResolver(JavaContext context, StatementVisitor.StatementVisitorInternal statementVisitor) {
        this.context = Objects.requireNonNull(context, "context cannot be null");
        this.statementVisitor = Objects.requireNonNull(statementVisitor, "statementVisitor cannot be null");
    }

    public Statement resolve(JavaParser.IfElseStatementContext ifCtx) {
        Expression condition = context.getVisitor(Expression.class).visit(ifCtx.cond, context);
        boolean condValue = resolveCondition(condition);
        List<Statement> visitedStatements = new ArrayList<>();
        if (condValue) {
            ifCtx.ifBranchContent(0).statement().forEach(stmtContext -> visitedStatements.add(statementVisitor.visit(stmtContext)));
        } else if (ifCtx.ifBranchContent(1) != null) {
            ifCtx.ifBranchContent(1).statement().forEach(stmtContext -> visitedStatements.add(statementVisitor.visit(stmtContext)));
            return IfElseStatement.createElse(visitedStatements);
        }
        return IfElseStatement.createIf(ifCtx.cond.getText(), visitedStatements);
    }

    boolean resolveCondition(Expression condition) {
        Value value = condition.getValue();
        if (value instanceof ConstantProvider && ((ConstantProvider) value).getConstant().c instanceof Boolean) {
            return (Boolean) ((ConstantProvider) value).getConstant().c;
        }
        throw new ResolvingException(value.getExpression().getText() + " nie jest typu logicznego");
    }
}