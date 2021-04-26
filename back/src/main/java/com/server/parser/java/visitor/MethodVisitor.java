package com.server.parser.java.visitor;

import com.server.parser.java.JavaParser;
import com.server.parser.java.ast.ConstructorHeader;
import com.server.parser.java.ast.Method;
import com.server.parser.java.ast.MethodHeader;
import com.server.parser.java.ast.statement.expression_statement.VariableDef;
import com.server.parser.java.context.ContextParameters;
import com.server.parser.java.context.JavaContext;
import com.server.parser.java.context.MethodContext;
import com.server.parser.java.variable.MethodVar;
import com.server.parser.util.exception.ResolvingException;
import org.antlr.v4.runtime.RuleContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MethodVisitor extends JavaVisitor<Method> {
    private final MethodContext context;

    MethodVisitor(JavaContext context) {
        this.context = (MethodContext) Objects.requireNonNull(context, "context cannot be null");
    }

    @Override
    public Method visitMethodDec(JavaParser.MethodDecContext ctx) {
        MethodHeader methodHeader = visit(ctx.methodHeader());
        return context.save(methodHeader, ctx.methodBody());
    }

    MethodHeader visit(JavaParser.MethodHeaderContext ctx) {
        List<String> modifiers = ctx.methodModifier().stream()
                .map(RuleContext::getText)
                .collect(Collectors.toList());
        String methodResult = ctx.methodResult().getText();
        String identifier = ctx.identifier().getText();
        List<VariableDef> args = visit(ctx.methodArgs());
        return new MethodHeader(modifiers, methodResult, identifier, args);
    }

    List<VariableDef> visit(JavaParser.MethodArgsContext ctx) {
        JavaVisitor<VariableDef> visitor = context.getVisitor(VariableDef.class, context);
        List<VariableDef> arguments = ctx.singleMethodArg().stream()
                .map(visitor::visit)
                .collect(Collectors.toList());
        putArgumentsIntoMethodContext(arguments);
        return arguments;
    }

    private void putArgumentsIntoMethodContext(List<VariableDef> args) {
        args.forEach(variableDef -> context.addVariable(new MethodVar(variableDef)));
    }

    @Override
    public Method visitConstructorDec(JavaParser.ConstructorDecContext ctx) {
        MethodHeader constructorHeader = visit(ctx.constructorHeader());
        return context.save(constructorHeader, ctx.methodBody());
    }

    MethodHeader visit(JavaParser.ConstructorHeaderContext ctx) {
        List<String> modifiers = new ArrayList<>();
        if (ctx.constructorModifier() != null) {
            modifiers.add(ctx.constructorModifier().getText());
        }
        String identifier = ctx.identifier().getText();
        ContextParameters parameters = context.getParameters();
        if (!identifier.equals(parameters.getClassName())) {
            throw new ResolvingException(
                    String.format("Konstruktor %s różny od nazwy klasy %s", identifier, parameters.getClassName()));
        }
        List<VariableDef> args = visit(ctx.methodArgs());
        return new ConstructorHeader(modifiers, identifier, args);
    }
}