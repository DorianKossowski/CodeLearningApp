package com.server.parser.java.task;

import com.server.parser.java.JavaTaskBaseListener;
import com.server.parser.java.JavaTaskParser;
import com.server.parser.java.task.model.*;
import com.server.parser.java.task.verifier.TaskVerifier;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class JavaTaskListener extends JavaTaskBaseListener {
    private final TaskVerifier taskVerifier;

    private ClassModel.Builder classBuilder;
    private FieldModel.Builder fieldBuilder;
    private MethodModel.Builder methodBuilder;
    private MethodModel.Builder constructorBuilder;
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
        methodBuilder.withName(convertToString(ctx.STRING_LITERAL()));
    }

    private String convertToString(TerminalNode terminalNode) {
        return JavaTaskGrammarHelper.getFromStringLiteral(terminalNode.getText());
    }

    @Override
    public void enterMethodArgsRuleSpec(JavaTaskParser.MethodArgsRuleSpecContext ctx) {
        List<MethodArgs> args = new ArrayList<>();
        if (ctx.argsElements() != null) {
            for (JavaTaskParser.ArgsElementContext argsContext : ctx.argsElements().argsElement()) {
                Optional<String> typeOptional = JavaTaskGrammarHelper.extractValue(argsContext.valueOrEmpty().get(0));
                Optional<String> nameOptional = JavaTaskGrammarHelper.extractValue(argsContext.valueOrEmpty().get(1));
                args.add(new MethodArgs(typeOptional.orElse(null), nameOptional.orElse(null)));
            }
        }
        if (ctx.getParent() instanceof JavaTaskParser.ConstructorRuleSpecContext) {
            constructorBuilder.withArgs(args);
        } else if (ctx.getParent() instanceof JavaTaskParser.MethodRuleSpecContext) {
            methodBuilder.withArgs(args);
        }
    }

    @Override
    public void enterModifiersRuleSpec(JavaTaskParser.ModifiersRuleSpecContext ctx) {
        List<String> modifiers = new ArrayList<>();
        if (ctx.STRING_LITERAL() != null) {
            modifiers = ctx.STRING_LITERAL().stream()
                    .map(this::convertToString)
                    .collect(Collectors.toList());
        }
        if (ctx.getParent() instanceof JavaTaskParser.MethodRuleSpecContext) {
            methodBuilder.withModifiers(modifiers);
        } else if (ctx.getParent() instanceof JavaTaskParser.FieldRuleSpecContext) {
            fieldBuilder.withModifiers(modifiers);
        } else if (ctx.getParent() instanceof JavaTaskParser.ClassRuleSpecContext) {
            classBuilder.withModifiers(modifiers);
        }
    }

    @Override
    public void enterMethodResultRuleSpec(JavaTaskParser.MethodResultRuleSpecContext ctx) {
        methodBuilder.withResult(convertToString(ctx.STRING_LITERAL()));
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
        statementBuilder.withMethod(convertToString(ctx.STRING_LITERAL()));
    }

    @Override
    public void enterTextRuleSpec(JavaTaskParser.TextRuleSpecContext ctx) {
        if (ctx.parent instanceof JavaTaskParser.StatementRuleSpecContext) {
            statementBuilder.withText(convertToString(ctx.STRING_LITERAL()));
        } else if (ctx.parent instanceof JavaTaskParser.VariableRuleSpecContext) {
            variableBuilder.withText(convertToString(ctx.STRING_LITERAL()));
        }
    }

    @Override
    public void enterStatementResolvedRuleSpec(JavaTaskParser.StatementResolvedRuleSpecContext ctx) {
        statementBuilder.withResolved(convertToString(ctx.STRING_LITERAL()));
    }

    @Override
    public void enterLogInfo(JavaTaskParser.LogInfoContext ctx) {
        String log = convertToString(ctx.STRING_LITERAL());
        if (ctx.parent instanceof JavaTaskParser.StatementRuleSpecContext) {
            statementBuilder.withLogInfo(log);
        } else if (ctx.parent instanceof JavaTaskParser.VariableRuleSpecContext) {
            variableBuilder.withLogInfo(log);
        } else if (ctx.parent instanceof JavaTaskParser.ClassRuleSpecContext) {
            classBuilder.withLogInfo(log);
        } else if (ctx.parent instanceof JavaTaskParser.FieldRuleSpecContext) {
            fieldBuilder.withLogInfo(log);
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
        statementBuilder.withIf(convertToString(ctx.STRING_LITERAL()));
    }

    @Override
    public void enterElseSpec(JavaTaskParser.ElseSpecContext ctx) {
        statementBuilder.isInElse(true);
    }

    @Override
    public void enterElseIfSpec(JavaTaskParser.ElseIfSpecContext ctx) {
        statementBuilder.withElseIf(convertToString(ctx.STRING_LITERAL()));
    }

    @Override
    public void enterSwitchExpr(JavaTaskParser.SwitchExprContext ctx) {
        statementBuilder.withSwitchExpr(convertToString(ctx.STRING_LITERAL()));
    }

    @Override
    public void enterSwitchLabel(JavaTaskParser.SwitchLabelContext ctx) {
        statementBuilder.withSwitchLabel(convertToString(ctx.STRING_LITERAL()));
    }

    @Override
    public void enterForIteration(JavaTaskParser.ForIterationContext ctx) {
        String stringLiteral = convertToString(ctx.STRING_LITERAL());
        statementBuilder.withForIteration(Integer.parseInt(stringLiteral));
    }

    @Override
    public void enterWhileIteration(JavaTaskParser.WhileIterationContext ctx) {
        String stringLiteral = convertToString(ctx.STRING_LITERAL());
        statementBuilder.withWhileIteration(Integer.parseInt(stringLiteral));
    }

    @Override
    public void enterDoWhileIteration(JavaTaskParser.DoWhileIterationContext ctx) {
        String stringLiteral = convertToString(ctx.STRING_LITERAL());
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
        classBuilder.withName(convertToString(ctx.STRING_LITERAL()));
    }

    @Override
    public void enterClassConstructorSpec(JavaTaskParser.ClassConstructorSpecContext ctx) {
        constructorBuilder = MethodModel.builder();
    }

    @Override
    public void exitClassConstructorSpec(JavaTaskParser.ClassConstructorSpecContext ctx) {
        taskVerifier.verifyConstructor(constructorBuilder.build());
    }

    @Override
    public void enterConstructorNameRuleSpec(JavaTaskParser.ConstructorNameRuleSpecContext ctx) {
        constructorBuilder.withName(convertToString(ctx.STRING_LITERAL()));
    }

    @Override
    public void enterClassFieldSpec(JavaTaskParser.ClassFieldSpecContext ctx) {
        fieldBuilder = FieldModel.builder();
    }

    @Override
    public void exitClassFieldSpec(JavaTaskParser.ClassFieldSpecContext ctx) {
        taskVerifier.verifyField(fieldBuilder.build());
    }

    @Override
    public void enterTypeRuleSpec(JavaTaskParser.TypeRuleSpecContext ctx) {
        fieldBuilder.withType(convertToString(ctx.STRING_LITERAL()));
    }

    @Override
    public void enterFieldNameRuleSpec(JavaTaskParser.FieldNameRuleSpecContext ctx) {
        fieldBuilder.withName(convertToString(ctx.STRING_LITERAL()));
    }

    @Override
    public void enterInitTextRuleSpec(JavaTaskParser.InitTextRuleSpecContext ctx) {
        fieldBuilder.withInitText(convertToString(ctx.STRING_LITERAL()));
    }
}