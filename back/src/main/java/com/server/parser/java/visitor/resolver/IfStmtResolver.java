package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ConstantProvider;
import com.server.parser.java.ast.expression.Expression;
import com.server.parser.java.ast.statement.IfElseStatement;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.ast.value.Value;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.java.visitor.StatementVisitor;
import com.server.parser.util.exception.ResolvingException;
import org.apache.commons.lang.SerializationUtils;

public class IfStmtResolver {

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
        JavaContext validationContext = (JavaContext) SerializationUtils.clone(context);
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

    static boolean resolveCondition(JavaContext context, JavaParser.ExpressionContext expressionContext) {
        Expression condition = context.getVisitor(Expression.class).visit(expressionContext, context);
        Value value = condition.getValue();
        if (value instanceof ConstantProvider && ((ConstantProvider) value).getConstant().c instanceof Boolean) {
            return (Boolean) ((ConstantProvider) value).getConstant().c;
        }
        throw new ResolvingException(value.getExpression().getText() + " nie jest typu logicznego");
    }
}