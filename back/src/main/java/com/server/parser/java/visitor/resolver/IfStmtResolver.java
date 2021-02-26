package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.IfElseStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.ContextFactory;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.java.visitor.StatementVisitor;
import com.server.parser.util.exception.ResolvingException;

public class IfStmtResolver extends StatementResolver {

    public static IfElseStatement resolve(JavaContext context, StatementVisitor.StatementVisitorInternal statementVisitor,
                                          JavaParser.IfElseStatementContext ifCtx) {
        validateBranchesContent(context, ifCtx);
        boolean condValue = resolveCondition(context, ifCtx.cond);
        Statement visitedStatement = null;
        if (condValue) {
            visitedStatement = statementVisitor.visit(ifCtx.statement(0));
        } else if (ifCtx.statement(1) != null) {
            return IfElseStatement.createElse(statementVisitor.visit(ifCtx.statement(1)));
        }
        return IfElseStatement.createIf(ifCtx.cond.getText(), visitedStatement);
    }

    static void validateBranchesContent(JavaContext context, JavaParser.IfElseStatementContext ifCtx) {
        JavaContext validationContext = ContextFactory.createValidationContext(context);
        JavaVisitor<Statement> visitor = validationContext.getVisitor(Statement.class);

        Statement ifContentStmt = visitor.visit(ifCtx.statement(0), validationContext);
        validateBranchContent(ifContentStmt, ifContentStmt.getText());
        if (ifCtx.statement(1) != null) {
            Statement elseContentStmt = visitor.visit(ifCtx.statement(1), validationContext);
            validateBranchContent(elseContentStmt, ifContentStmt.getText());
        }
    }

    private static void validateBranchContent(Statement ifContentStmt, String text) {
        if (ifContentStmt instanceof VariableDef) {
            throw new ResolvingException(String.format("Deklaracja %s nie jest w tym miejscu dozwolona", text));
        }
    }
}