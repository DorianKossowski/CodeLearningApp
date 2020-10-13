package com.server.parser.java.visitor;

import com.server.parser.java.JavaBaseVisitor;
import com.server.parser.java.JavaGrammarHelper;
import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.*;
import com.server.parser.java.context.JavaContext;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.List;
import java.util.Objects;

public class StatementVisitor extends JavaVisitor<Statement> {

    @Override
    public Statement visit(ParserRuleContext ctx, JavaContext context) {
        return new MethodBodyStatementVisitorInternal(context).visit(ctx);
    }

    private static class MethodBodyStatementVisitorInternal extends JavaBaseVisitor<Statement> {
        private final JavaContext context;

        private MethodBodyStatementVisitorInternal(JavaContext context) {
            this.context = Objects.requireNonNull(context, "context cannot be null");
        }

        //*** METHOD CALL ***//
        @Override
        public MethodCall visitMethodCall(JavaParser.MethodCallContext ctx) {
            String methodName = new MethodNameVisitor().visit(ctx.methodName());
            List<Expression> arguments = new CallArgumentsVisitor().visit(ctx.callArguments(), context);
            enhanceArguments(arguments);
            return new MethodCall(JavaGrammarHelper.getOriginalText(ctx), context.getCurrentMethodContext().getMethodName(), methodName,
                    arguments);
        }

        void enhanceArguments(List<Expression> arguments) {
            arguments.stream()
                    .filter(expression -> expression instanceof ObjectRef)
                    .map(expression -> ((ObjectRef) expression))
                    .forEach(objectRef -> objectRef.setValue(context.getCurrentMethodContext().getValue(objectRef.getText())));
        }

        //*** VARIABLE ***//
        @Override
        public Statement visitMethodVarDec(JavaParser.MethodVarDecContext ctx) {
            Variable variable = (Variable) visit(ctx.varDec());
            context.getCurrentMethodContext().addVar(variable.getName(), variable.getValue().getText());
            return variable;
        }

        @Override
        public Statement visitFieldDec(JavaParser.FieldDecContext ctx) {
            return visit(ctx.varDec());
        }

        @Override
        public Statement visitSingleMethodArg(JavaParser.SingleMethodArgContext ctx) {
            String type = textVisitor.visit(ctx.type());
            String id = textVisitor.visit(ctx.identifier());
            return new Variable(JavaGrammarHelper.getOriginalText(ctx), type, id);
        }

        @Override
        public Statement visitVarDec(JavaParser.VarDecContext ctx) {
            String id = textVisitor.visit(ctx.identifier());
            Expression expression = null;
            if (ctx.expression() != null) {
                expression = context.getVisitor(Expression.class).visit(ctx.expression(), context);
            }
            return new Variable(JavaGrammarHelper.getOriginalText(ctx), textVisitor.visit(ctx.type()), id, expression);
        }
    }
}