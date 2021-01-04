package com.server.parser.java.task;

import com.server.parser.java.JavaTaskBaseListener;
import com.server.parser.java.JavaTaskParser;
import com.server.parser.java.task.model.*;
import com.server.parser.java.task.verifier.TaskVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaTaskListener extends JavaTaskBaseListener {
    private final TaskVerifier taskVerifier;

    private ClassModel.Builder classBuilder;
    private MethodModel.Builder methodBuilder;
    private StatementModel.Builder statementBuilder;
    private VariableModel.Builder variableBuilder;

    public JavaTaskListener(TaskVerifier taskVerifier) {
        this.taskVerifier = Objects.requireNonNull(taskVerifier, "taskVerifier cannot be null");
    }

    @Override
    public void enterMethodRule(JavaTaskParser.MethodRuleContext ctx) {
        methodBuilder = MethodModel.builder();
    }

    @Override
    public void exitMethodRule(JavaTaskParser.MethodRuleContext ctx) {
        taskVerifier.verifyMethod(methodBuilder.build());
    }

    @Override
    public void enterMethodNameRuleSpec(JavaTaskParser.MethodNameRuleSpecContext ctx) {
        JavaTaskGrammarHelper.extractValue(ctx.valueOrEmpty()).ifPresent(value -> methodBuilder.withName(value));
    }

    @Override
    public void enterMethodArgsRuleSpec(JavaTaskParser.MethodArgsRuleSpecContext ctx) {
        List<MethodArgs> args = new ArrayList<>();
        for (JavaTaskParser.ArgsElementContext argsContext : ctx.argsElement()) {
            Optional<String> typeOptional = JavaTaskGrammarHelper.extractValue(argsContext.valueOrEmpty().get(0));
            Optional<String> nameOptional = JavaTaskGrammarHelper.extractValue(argsContext.valueOrEmpty().get(1));
            args.add(new MethodArgs(typeOptional.orElse(null), nameOptional.orElse(null)));
        }
        methodBuilder.withArgs(args);
    }

    @Override
    public void enterMethodModifiersRuleSpec(JavaTaskParser.MethodModifiersRuleSpecContext ctx) {
        List<String> modifiers = ctx.STRING_LITERAL().stream()
                .map(node -> JavaTaskGrammarHelper.getFromStringLiteral(node.getText()))
                .collect(Collectors.toList());
        methodBuilder.withModifiers(modifiers);
    }

    @Override
    public void enterMethodResultRuleSpec(JavaTaskParser.MethodResultRuleSpecContext ctx) {
        methodBuilder.withResult(JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText()));
    }

    @Override
    public void enterStatementRule(JavaTaskParser.StatementRuleContext ctx) {
        statementBuilder = StatementModel.builder();
    }

    @Override
    public void exitStatementRule(JavaTaskParser.StatementRuleContext ctx) {
        taskVerifier.verifyStatement(statementBuilder.build());
    }

    @Override
    public void enterStatementMethodRuleSpec(JavaTaskParser.StatementMethodRuleSpecContext ctx) {
        JavaTaskGrammarHelper.extractValue(ctx.valueOrEmpty()).ifPresent(value -> statementBuilder.withMethod(value));
    }

    @Override
    public void enterTextRuleSpec(JavaTaskParser.TextRuleSpecContext ctx) {
        if (ctx.parent instanceof JavaTaskParser.StatementRuleSpecContext) {
            statementBuilder.withText(JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText()));
        } else if (ctx.parent instanceof JavaTaskParser.VariableRuleSpecContext) {
            variableBuilder.withText(JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText()));
        }
    }

    @Override
    public void enterStatementResolvedRuleSpec(JavaTaskParser.StatementResolvedRuleSpecContext ctx) {
        statementBuilder.withResolved(JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText()));
    }

    @Override
    public void enterLogInfo(JavaTaskParser.LogInfoContext ctx) {
        if (ctx.parent instanceof JavaTaskParser.StatementRuleSpecContext) {
            statementBuilder.withLogInfo(JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText()));
        } else if (ctx.parent instanceof JavaTaskParser.VariableRuleSpecContext) {
            variableBuilder.withLogInfo(JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText()));
        } else if (ctx.parent instanceof JavaTaskParser.ClassRuleSpecContext) {
            classBuilder.withLogInfo(JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText()));
        }
    }

    @Override
    public void enterVariableRule(JavaTaskParser.VariableRuleContext ctx) {
        variableBuilder = VariableModel.builder();
    }

    @Override
    public void exitVariableRule(JavaTaskParser.VariableRuleContext ctx) {
        taskVerifier.verifyVariable(variableBuilder.build());
    }

    @Override
    public void enterIfSpec(JavaTaskParser.IfSpecContext ctx) {
        statementBuilder.withIf(JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText()));
    }

    @Override
    public void enterElseSpec(JavaTaskParser.ElseSpecContext ctx) {
        statementBuilder.isInElse(true);
    }

    @Override
    public void enterElseIfSpec(JavaTaskParser.ElseIfSpecContext ctx) {
        statementBuilder.withElseIf(JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText()));
    }

    @Override
    public void enterSwitchExpr(JavaTaskParser.SwitchExprContext ctx) {
        statementBuilder.withSwitchExpr(JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText()));
    }

    @Override
    public void enterSwitchLabel(JavaTaskParser.SwitchLabelContext ctx) {
        statementBuilder.withSwitchLabel(JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText()));
    }

    @Override
    public void enterForIteration(JavaTaskParser.ForIterationContext ctx) {
        String stringLiteral = JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText());
        statementBuilder.withForIteration(Integer.parseInt(stringLiteral));
    }

    @Override
    public void enterWhileIteration(JavaTaskParser.WhileIterationContext ctx) {
        String stringLiteral = JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText());
        statementBuilder.withWhileIteration(Integer.parseInt(stringLiteral));
    }

    @Override
    public void enterDoWhileIteration(JavaTaskParser.DoWhileIterationContext ctx) {
        String stringLiteral = JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText());
        statementBuilder.withDoWhileIteration(Integer.parseInt(stringLiteral));
    }

    @Override
    public void enterClassRule(JavaTaskParser.ClassRuleContext ctx) {
        classBuilder = ClassModel.builder();
    }

    @Override
    public void exitClassRule(JavaTaskParser.ClassRuleContext ctx) {
        taskVerifier.verifyClass(classBuilder.build());
    }

    @Override
    public void enterClassNameSpec(JavaTaskParser.ClassNameSpecContext ctx) {
        classBuilder.withName(JavaTaskGrammarHelper.getFromStringLiteral(ctx.STRING_LITERAL().getText()));
    }
}