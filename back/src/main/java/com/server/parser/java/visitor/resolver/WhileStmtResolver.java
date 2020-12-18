package com.server.parser.java.visitor.resolver;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.statement.Statement;
import com.server.parser.java.ast.statement.VariableDef;
import com.server.parser.java.ast.statement.WhileStatement;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.visitor.JavaVisitor;
import com.server.parser.util.exception.ResolvingException;
import org.apache.commons.lang.SerializationUtils;

public class WhileStmtResolver extends LoopResolver {

    public static WhileStatement resolve(JavaContext context, JavaParser.WhileStatementContext whileCtx) {
        validateContent(context, whileCtx.statement());
        return new WhileStatement(null);
    }

    static void validateContent(JavaContext context, JavaParser.StatementContext statementContext) {
        JavaContext validationContext = (JavaContext) SerializationUtils.clone(context);
        JavaVisitor<Statement> visitor = validationContext.getVisitor(Statement.class);
        Statement statement = visitor.visit(statementContext, validationContext);
        if (statement instanceof VariableDef) {
            throw new ResolvingException(String.format("Deklaracja %s nie jest w tym miejscu dozwolona", statement.getText()));
        }
    }
}